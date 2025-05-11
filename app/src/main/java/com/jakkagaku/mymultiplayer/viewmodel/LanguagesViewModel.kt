package com.jakkagaku.mymultiplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jakkagaku.mymultiplayer.model.LanguageModel
import com.jakkagaku.mymultiplayer.repository.LanguageRepository
import com.jakkagaku.mymultiplayer.retrofit.LanguageApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguagesViewModel @Inject constructor(
    private val repository: LanguageRepository
) : ViewModel() {

    private val _languageState = MutableStateFlow<LanguageApiResult<List<LanguageModel>>>(LanguageApiResult.Loading)
    val languageState: StateFlow<LanguageApiResult<List<LanguageModel>>> = _languageState

    init {
        fetchLanguages()
    }

    fun fetchLanguages() {
        viewModelScope.launch {
            _languageState.value = LanguageApiResult.Loading
            _languageState.value = repository.getLanguages()
        }
    }
}