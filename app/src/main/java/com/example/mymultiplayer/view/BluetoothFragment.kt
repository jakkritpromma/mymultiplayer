package com.example.mymultiplayer.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mymultiplayer.R
import com.example.mymultiplayer.databinding.FragmentBluetoothBinding
import com.example.mymultiplayer.model.BtDeviceInfoModel
import com.example.mymultiplayer.viewmodel.BtViewModel

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
            val btAddress = (parent.getItemAtPosition(pos) as String).lines()[1]
            btViewModel.selectDevice(btAddress)
        }

        val devicesObserver = Observer<List<BtDeviceInfoModel>> { newDevices ->
            if (newDevices != null) {
                val deviceLabels = newDevices.map {
                    """|${it.device.name ?: "Unknown Name"} ${it.device.address} BOUND: ${it.bound}
                """.trimMargin()
                }
                binding?.deviceList?.adapter = ArrayAdapter(requireContext(), R.layout.device_list_item, R.id.textview_item, deviceLabels)
            }
        }
        btViewModel.devices.observe(viewLifecycleOwner, devicesObserver)

        binding?.tvBack?.setOnClickListener {
            findNavController().navigate(R.id.action_bluetoothFragment_to_mainFragment)
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