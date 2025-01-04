package com.jakkagaku.mymultiplayer.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jakkagaku.mymultiplayer.R
import com.jakkagaku.mymultiplayer.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    private val TAG = SettingFragment::class.simpleName
    private var binding: FragmentSettingBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        binding?.tvLanguages?.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_languagesFragment)
        }
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        super.onDestroyView()
        binding = null
    }
}