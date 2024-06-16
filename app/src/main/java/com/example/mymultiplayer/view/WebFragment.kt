package com.example.mymultiplayer.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mymultiplayer.R
import com.example.mymultiplayer.databinding.FragmentWebBinding

class WebFragment: Fragment() {
    private val TAG = WebFragment::class.simpleName
    private var binding: FragmentWebBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        binding = FragmentWebBinding.inflate(inflater, container, false)
        val view = binding?.root

        binding?.tvBackWeb?.setOnClickListener {
            findNavController().navigate(R.id.action_webFragment_to_mainFragment)
        }
        
        binding?.webView?.webViewClient = WebViewClient()
        binding?.webView?.loadUrl("https://www.google.com/")
        binding?.webView?.settings?.javaScriptEnabled = true
        binding?.webView?.settings?.setSupportZoom(true)

        return view
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        super.onDestroyView()
        binding = null
    }

}