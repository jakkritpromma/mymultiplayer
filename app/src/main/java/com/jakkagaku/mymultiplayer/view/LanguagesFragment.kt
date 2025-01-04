package com.jakkagaku.mymultiplayer.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakkagaku.mymultiplayer.R
import com.jakkagaku.mymultiplayer.adapter.LanguageListAdapter
import com.jakkagaku.mymultiplayer.databinding.FragmentLanguagesBinding
import com.jakkagaku.mymultiplayer.view.compose.LanguagesScreen
import com.jakkagaku.mymultiplayer.viewmodel.LanguagesViewModel

class LanguagesFragment : Fragment() {
    private val TAG = LanguagesFragment::class.simpleName
    private var binding: FragmentLanguagesBinding? = null
    private lateinit var recyclerAdapter: LanguageListAdapter

    @SuppressLint("NotifyDataSetChanged") override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        binding = FragmentLanguagesBinding.inflate(inflater, container, false)
        binding?.countryListRecyclerview?.layoutManager = LinearLayoutManager(requireActivity())
        recyclerAdapter = LanguageListAdapter(requireActivity())
        binding?.countryListRecyclerview?.adapter = recyclerAdapter

        val viewModel: LanguagesViewModel = ViewModelProvider(this).get(LanguagesViewModel::class.java)
        /*viewModel.getLiveDataObserver().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                recyclerAdapter.setCountryList(it)
                recyclerAdapter.notifyDataSetChanged()
            } else {
                requireActivity().runOnUiThread(Runnable {
                    Toast.makeText(requireActivity(), "Error in getting list", Toast.LENGTH_SHORT).show()
                })
            }
        })*/
        viewModel.makeAPICall(requireActivity())

        binding?.tvBackFromLanguages?.setOnClickListener {
            findNavController().navigate(R.id.action_langaugesFragment_to_settingFragment)
        }

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                LanguagesScreen(findNavController(), viewModel)
            }
        }
    }


    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        super.onDestroyView()
        binding = null
    }
}