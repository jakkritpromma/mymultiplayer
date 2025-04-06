package com.jakkagaku.mymultiplayer.retrofit

sealed class LanguageApiResult<out T> {
    object Loading : LanguageApiResult<Nothing>()
    data class Success<T>(val data: T) : LanguageApiResult<T>()
    data class Error(val message: String) : LanguageApiResult<Nothing>()
}