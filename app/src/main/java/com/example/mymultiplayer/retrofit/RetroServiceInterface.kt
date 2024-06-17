package com.example.mymultiplayer.retrofit

import com.example.mymultiplayer.model.LanguageModel
import retrofit2.Call
import retrofit2.http.GET

interface RetroServiceInterface {
    @GET("all?fields=flags,name,languages")
    fun getAllCountryFlagLang(): Call<List<LanguageModel>>
}