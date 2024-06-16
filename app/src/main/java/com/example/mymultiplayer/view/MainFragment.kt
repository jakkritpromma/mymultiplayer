package com.example.mymultiplayer.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mymultiplayer.R
import com.example.mymultiplayer.databinding.FragmentMainBinding


class MainFragment : Fragment() {
    private val TAG = MainFragment::class.simpleName
    private var binding: FragmentMainBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        binding?.tvMediaPlayer?.setOnClickListener { }
        binding?.tvBluetooth?.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_bluetoothFragment)
        }
        binding?.tvBrowser?.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_webFragment)
        }
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        super.onDestroyView()
        binding = null
    }
}