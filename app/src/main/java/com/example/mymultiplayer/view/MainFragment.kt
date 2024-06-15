package com.example.mymultiplayer.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mymultiplayer.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private val TAG = MainFragment::class.simpleName
    private var binding: FragmentMainBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        binding?.tvMediaPlayer?.setOnClickListener {  }
        binding?.tvBluetooth?.setOnClickListener {  }
        binding?.tvBrowser?.setOnClickListener {  }
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        super.onDestroyView()
        binding = null
    }

}