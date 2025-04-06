package com.jakkagaku.mymultiplayer.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jakkagaku.mymultiplayer.model.LanguageModel
import com.jakkagaku.mymultiplayer.repository.LanguageRepository
import com.jakkagaku.mymultiplayer.retrofit.LanguageApiResult
import kotlinx.coroutines.launch

class LanguagesViewModel(private val repository: LanguageRepository) : ViewModel() {
    var languageState by mutableStateOf<LanguageApiResult<List<LanguageModel>>>(LanguageApiResult.Loading)
        private set

    init {
        fetchLanguages()
    }

    fun fetchLanguages() {
        viewModelScope.launch {
            languageState = LanguageApiResult.Loading
            languageState = repository.getLanguages()
        }
    }
}