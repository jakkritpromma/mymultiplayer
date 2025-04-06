package com.jakkagaku.mymultiplayer.retrofit

import com.jakkagaku.mymultiplayer.model.LanguageModel
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("all?fields=flags,name,languages")
    suspend fun getLanguages(): Response<List<LanguageModel>>
}