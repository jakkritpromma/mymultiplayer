package com.jakkagaku.mymultiplayer.repository

import com.jakkagaku.mymultiplayer.model.LanguageModel
import com.jakkagaku.mymultiplayer.retrofit.ApiService
import com.jakkagaku.mymultiplayer.retrofit.LanguageApiResult

class LanguageRepository(private val apiService: ApiService) {
    suspend fun getLanguages(): LanguageApiResult<List<LanguageModel>> {
        return try {
            val response = apiService.getLanguages()
            if (response.isSuccessful) {
                LanguageApiResult.Success(response.body() ?: emptyList())
            } else {
                LanguageApiResult.Error(response.errorBody()?.string() ?: "Failed response")
            }
        } catch (e: Exception) {
            LanguageApiResult.Error(e.localizedMessage ?: "Error")
        }
    }
}
