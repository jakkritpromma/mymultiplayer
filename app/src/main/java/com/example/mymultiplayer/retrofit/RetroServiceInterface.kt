package com.example.mymultiplayer.retrofit

import com.example.mymultiplayer.model.CountryModel
import retrofit2.Call
import retrofit2.http.GET

interface RetroServiceInterface {
    @GET("v2")
    fun getCountryList(): Call<List<CountryModel>>
}