package com.jakkagaku.mymultiplayer.view

import android.Manifest.permission
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jakkagaku.mymultiplayer.R
import com.jakkagaku.mymultiplayer.databinding.FragmentBluetoothBinding
import com.jakkagaku.mymultiplayer.model.BtDeviceInfoModel
import com.jakkagaku.mymultiplayer.viewmodel.BtViewModel

class BluetoothFragment : Fragment() {
    private val TAG = BluetoothFragment::class.simpleName
    private var binding: FragmentBluetoothBinding? = null
    private lateinit var btViewModel: BtViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        binding = FragmentBluetoothBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        btViewModel = ViewModelProvider(requireActivity()).get(BtViewModel::class.java)

        binding?.deviceList?.setOnItemClickListener { parent, _, pos, _ ->
            Log.d(TAG, "lines().size:" + (parent.getItemAtPosition(pos) as String).lines().size)
            Log.d(TAG, "lines().get(0):" + (parent.getItemAtPosition(pos) as String).lines().get(0))
            val item = (parent.getItemAtPosition(pos) as String).lines()[0]
            Log.d(TAG, "lines()[0]:" + item)
            val MacAddress = extractMacAddress(item)
            Log.d(TAG, "MacAddress: " + MacAddress);
            btViewModel.selectDevice(MacAddress!!)
        }

        val devicesObserver = Observer<List<BtDeviceInfoModel>> { newDevices ->
            if (newDevices != null) {
                val deviceLabels = newDevices.map {
                    if (context?.let { it1 -> ActivityCompat.checkSelfPermission(it1.applicationContext, permission.BLUETOOTH_CONNECT) } != PackageManager.PERMISSION_GRANTED) { // TODO: Consider calling
                        return@Observer
                    }
                    """|${it.device.name ?: "Unknown Name"} Address: ${it.device.address} BOUND: ${it.bound}""".trimMargin()
                }
                binding?.deviceList?.adapter = ArrayAdapter(requireContext(), R.layout.device_list_item, R.id.textview_item, deviceLabels)
            }
        }
        btViewModel.devices.observe(viewLifecycleOwner, devicesObserver)

        binding?.tvBack?.setOnClickListener {
            findNavController().navigate(R.id.action_bluetoothFragment_to_mainFragment)
        }
    }

    fun extractMacAddress(input: String): String? {
        val start = input.indexOf("Address: ") + "Address: ".length
        val end = input.indexOf(" BOUND")
        return if (start != -1 && end != -1) {
            input.substring(start, end)
        } else {
            ""
        }
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
        super.onStart()
        btViewModel.startDiscovering()
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        btViewModel.stopDiscovering()
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        super.onDestroyView()
        binding = null
    }
}