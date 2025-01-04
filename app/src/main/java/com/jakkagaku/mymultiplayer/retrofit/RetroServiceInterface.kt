package com.jakkagaku.mymultiplayer.retrofit

import com.jakkagaku.mymultiplayer.model.LanguageModel
import retrofit2.Call
import retrofit2.http.GET

interface RetroServiceInterface {
    @GET("all?fields=flags,name,languages")
    fun getAllCountryFlagLang(): Call<List<LanguageModel>>
}