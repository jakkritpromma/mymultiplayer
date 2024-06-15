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
    private var _binding: FragmentMainBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = _binding!!.root
        return view
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        super.onDestroyView()
        _binding = null
    }

}