package com.example.mymultiplayer.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.example.mymultiplayer.R
import com.example.mymultiplayer.databinding.FragmentBluetoothBinding


class BluetoothFragment : Fragment() {
    private val TAG = BluetoothFragment::class.simpleName
    private var binding: FragmentBluetoothBinding? = null
    private var m_bluetoothAdapter: BluetoothAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        binding = FragmentBluetoothBinding.inflate(inflater, container, false)
        val view = binding?.root

        binding?.tvBack?.setOnClickListener {
            findNavController().navigate(R.id.action_bluetoothFragment_to_mainFragment)
        }

        val bluetoothManager = context?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        m_bluetoothAdapter = bluetoothManager.adapter
        if (m_bluetoothAdapter == null) {
            Toast.makeText(activity, "this device doesn't support bluetooth", Toast.LENGTH_LONG).show()
        }
        Log.d(TAG, "!m_bluetoothAdapter!!.isEnabled: " + !m_bluetoothAdapter!!.isEnabled)
        if (!m_bluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestEnableBluetooth.launch(enableBtIntent)
        } else {
            btScan(requireActivity())
        }

        binding!!.tvRefresh.setOnClickListener {
            btScan(requireActivity())
        }
        return view
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        super.onDestroyView()
        binding = null
    }

    fun btScan(fragmentActivity: FragmentActivity) {
        Log.d(TAG, "Bt Adapter is ENABLED!")
        val builder = AlertDialog.Builder(fragmentActivity)
        builder.setView(R.layout.progress_layout)
        val progressDialog = builder.create()
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        progressDialog.show()
        var data: MutableList<Map<String?, Any?>?>? = ArrayList()
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                        Log.d(TAG, "Device discovery started")
                    }

                    BluetoothDevice.ACTION_FOUND -> {
                        Log.d(TAG, "found")
                        val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                        if (ActivityCompat.checkSelfPermission(fragmentActivity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) { // TODO: Consider calling
                            return
                        } else {
                            val deviceName = device?.name
                            val address = device?.address // MAC address
                            Log.d(TAG, "deviceName: $deviceName address: $address")
                            val dataNum: MutableMap<String?, Any?> = HashMap()
                            dataNum["A"] = deviceName
                            dataNum["B"] = address
                            dataNum["C"] = deviceName + " " + address
                            data?.add(dataNum)
                        }
                    }

                    BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                        Log.d(TAG, "Device discovery finished")
                        val fromWhere = arrayOf("C")
                        val listView = binding?.selectDeviceList
                        val itemName = intArrayOf(R.id.item_name)
                        val simpleAdapter = SimpleAdapter(fragmentActivity, data, R.layout.item_list, fromWhere, itemName)
                        listView!!.adapter = simpleAdapter
                        simpleAdapter.notifyDataSetChanged()
                        listView.onItemClickListener = AdapterView.OnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long -> //TODO connect
                        }
                        progressDialog.dismiss()
                    }
                }
            }
        } // Register for broadcasts when a device is discovered.
        val intentFilter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        }
        if (m_bluetoothAdapter?.isDiscovering() == true) {
            m_bluetoothAdapter?.cancelDiscovery();
        }
        fragmentActivity.registerReceiver(receiver, intentFilter)
        m_bluetoothAdapter?.startDiscovery()

    }

    @SuppressLint("MissingPermission") private fun pairedBtScan(fragmentActivity: FragmentActivity) {
        Log.d(TAG, "btScan")
        val listView = binding?.selectDeviceList
        val pairedDevices: Set<BluetoothDevice> = m_bluetoothAdapter?.bondedDevices as Set<BluetoothDevice>
        val simpleAdapter: SimpleAdapter
        var data: MutableList<Map<String?, Any?>?>? = null
        data = ArrayList()
        if (pairedDevices.isNotEmpty()) {
            val dataNum1: MutableMap<String?, Any?> = HashMap()
            dataNum1["A"] = ""
            dataNum1["B"] = ""
            dataNum1["C"] = ""
            data.add(dataNum1)
            for (device in pairedDevices) {
                val datanum: MutableMap<String?, Any?> = HashMap()
                datanum["A"] = device.name
                datanum["B"] = device.address
                datanum["C"] = device.name + " " + device.address
                data.add(datanum)
            }
            val fromWhere = arrayOf("C")
            val itemName = intArrayOf(R.id.item_name)
            simpleAdapter = SimpleAdapter(fragmentActivity, data, R.layout.item_list, fromWhere, itemName)
            listView!!.adapter = simpleAdapter
            simpleAdapter.notifyDataSetChanged()
            listView.onItemClickListener = AdapterView.OnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long -> //TODO connect paired
            }
        } else {
            Toast.makeText(fragmentActivity, "No device found", Toast.LENGTH_LONG).show()
            return
        }
    }

    private val requestEnableBluetooth = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) { // granted
            btScan(requireActivity())
        } else { // denied
        }
    }
}