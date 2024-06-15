package com.example.mymultiplayer.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymultiplayer.model.CountryModel
import com.example.mymultiplayer.retrofit.RetroInstance
import com.example.mymultiplayer.retrofit.RetroServiceInterface
import com.example.mymultiplayer.view.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityViewModel : ViewModel() {
    private val TAG = MainActivityViewModel::class.simpleName
    lateinit var liveDataList: MutableLiveData<List<CountryModel>>

    init {
        liveDataList = MutableLiveData()
    }

    fun getLiveDataObserver(): MutableLiveData<List<CountryModel>> {
        return liveDataList
    }

    fun makeAPICall() {
        val retroInstance = RetroInstance.getRetroInstance()
        val retroService = retroInstance.create(RetroServiceInterface::class.java)
        val call = retroService.getAllCountryFlagLang()
        call.enqueue(object: Callback<List<CountryModel>>{
            override fun onResponse(p0: Call<List<CountryModel>>, response: Response<List<CountryModel>>) {
                Log.d(TAG, "response.body: ${response.body()}")
                liveDataList.postValue(response.body())
            }

            override fun onFailure(p0: Call<List<CountryModel>>, t: Throwable) {
                Log.d(TAG, "t: ${t.message}")
                /*MainActivity.context.runOnUiThread(object: Runnable{
                    override fun run() {
                        Toast.makeText(MainActivity.context, t.message.toString(), Toast.LENGTH_LONG).show()
                    }
                })*/
                liveDataList.postValue(null)
            }
        })
    }
}