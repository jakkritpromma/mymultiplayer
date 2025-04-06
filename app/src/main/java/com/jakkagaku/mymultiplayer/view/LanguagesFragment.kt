package com.jakkagaku.mymultiplayer.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakkagaku.mymultiplayer.R
import com.jakkagaku.mymultiplayer.adapter.LanguageListAdapter
import com.jakkagaku.mymultiplayer.databinding.FragmentLanguagesBinding
import com.jakkagaku.mymultiplayer.repository.LanguageRepository
import com.jakkagaku.mymultiplayer.retrofit.ApiService
import com.jakkagaku.mymultiplayer.retrofit.RetroInstance
import com.jakkagaku.mymultiplayer.view.compose.LanguageListScreen
import com.jakkagaku.mymultiplayer.viewmodel.LanguagesViewModel

class LanguagesFragment : Fragment() {
    private val TAG = LanguagesFragment::class.simpleName
    private var binding: FragmentLanguagesBinding? = null
    private lateinit var recyclerAdapter: LanguageListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        binding = FragmentLanguagesBinding.inflate(inflater, container, false)
        binding?.countryListRecyclerview?.layoutManager = LinearLayoutManager(requireActivity())
        recyclerAdapter = LanguageListAdapter(requireActivity())
        binding?.countryListRecyclerview?.adapter = recyclerAdapter
        binding?.tvBackFromLanguages?.setOnClickListener {
            findNavController().navigate(R.id.action_langaugesFragment_to_settingFragment)
        }

        val retroInstance = RetroInstance.getRetroInstance()
        val apiService = retroInstance.create(ApiService::class.java)
        val repo = LanguageRepository(apiService)
        val viewModel = LanguagesViewModel(repo)

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                LanguageListScreen(findNavController(), viewModel)
            }
        }
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        super.onDestroyView()
        binding = null
    }
}