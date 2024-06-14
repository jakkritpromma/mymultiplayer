package com.example.mymultiplayer.retrofit

import com.example.mymultiplayer.model.CountryModel
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.http.GET

interface RetroServiceInterface {
    @GET("v2")
    fun getCountryList(): Call<List<CountryModel>>

    @GET("all")
    fun getAllCountryInfo(): Call<JsonArray>
}