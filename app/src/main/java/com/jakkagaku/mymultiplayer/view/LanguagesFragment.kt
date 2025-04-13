package com.jakkagaku.mymultiplayer.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jakkagaku.mymultiplayer.databinding.FragmentLanguagesBinding
import com.jakkagaku.mymultiplayer.view.compose.LanguageListScreen
import com.jakkagaku.mymultiplayer.viewmodel.LanguagesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguagesFragment : Fragment() {
    private val TAG = LanguagesFragment::class.simpleName
    private var binding: FragmentLanguagesBinding? = null
    private val viewModel: LanguagesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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