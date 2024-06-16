package com.example.mymultiplayer.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
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

        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (m_bluetoothAdapter == null) {
            Toast.makeText(activity, "this device doesn't support bluetooth", Toast.LENGTH_LONG).show()
        }
        Log.d(TAG, "!m_bluetoothAdapter!!.isEnabled: " + !m_bluetoothAdapter!!.isEnabled)
        if (!m_bluetoothAdapter!!.isEnabled) {
            requestBluetooth()
        } else {
            btScan()
        }

        binding!!.tvRefresh.setOnClickListener {

        }
        return view
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        super.onDestroyView()
        binding = null
    }

    @SuppressLint("MissingPermission") private fun btScan() {
        Log.d(TAG, "btScan")
        val bluetoothManager: BluetoothManager = MainActivity.mainActivity.getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        val builder = AlertDialog.Builder(MainActivity.mainActivity)
        val inflater = layoutInflater
        val dialogView: View = inflater.inflate(R.layout.scan_bt, null)
        builder.setCancelable(false)
        builder.setView(dialogView)
        val listView = dialogView.findViewById<ListView>(R.id.bt_lst)
        val dialog = builder.create()
        val pairedDevices: Set<BluetoothDevice> = bluetoothAdapter?.bondedDevices as Set<BluetoothDevice>
        val simpleAdapter: SimpleAdapter
        var data: MutableList<Map<String?, Any?>?>? = null
        data = ArrayList()
        if (pairedDevices.isNotEmpty()) {
            val dataNum1: MutableMap<String?, Any?> = HashMap()
            dataNum1["A"] = ""
            dataNum1["B"] = ""
            data.add(dataNum1)
            for (device in pairedDevices) {
                val datanum: MutableMap<String?, Any?> = HashMap()
                datanum["A"] = device.name
                datanum["B"] = device.address
                data.add(datanum)
            }
            val fromWhere = arrayOf("A")
            val viewswhere = intArrayOf(R.id.item_name)
            simpleAdapter = SimpleAdapter(MainActivity.mainActivity, data, R.layout.item_list, fromWhere, viewswhere)
            listView.adapter = simpleAdapter
            simpleAdapter.notifyDataSetChanged()
            listView.onItemClickListener = AdapterView.OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                val string = simpleAdapter.getItem(position) as HashMap<String, String>
                val name = string["A"]
                val address = string["B"]
                Log.d(TAG, "name: $name")
                Log.d(TAG, "address: $address")
                dialog.dismiss()
            }
        } else {
            Toast.makeText(MainActivity.mainActivity, "No device found", Toast.LENGTH_LONG).show()
            return
        }
        dialog.show()
    }

    private fun requestBluetooth() { // check android 12+
        Log.d(TAG, "requestBluetooth")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissions.launch(arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
            ))
        } else {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestEnableBluetooth.launch(enableBtIntent)
        }
    }

    private val requestEnableBluetooth = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) { // granted
            btScan()
        } else { // denied
        }
    }

    private val requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        permissions.entries.forEach {
            Log.d("MyTag", "${it.key} = ${it.value}")
        }
    }
}