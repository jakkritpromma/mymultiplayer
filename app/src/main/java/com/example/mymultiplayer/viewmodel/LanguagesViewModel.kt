package com.example.mymultiplayer.viewmodel

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.WindowManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymultiplayer.R
import com.example.mymultiplayer.model.LanguageModel
import com.example.mymultiplayer.retrofit.RetroInstance
import com.example.mymultiplayer.retrofit.RetroServiceInterface
import com.example.mymultiplayer.view.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LanguagesViewModel : ViewModel() {
    private val TAG = LanguagesViewModel::class.simpleName
    var liveDataList: MutableLiveData<List<LanguageModel>?> = MutableLiveData()

    fun getLiveDataObserver(): MutableLiveData<List<LanguageModel>?> {
        return liveDataList
    }

    fun makeAPICall() {
        Log.d(TAG, "makeAPICall")
        val builder = AlertDialog.Builder(MainActivity.mainActivity)
        builder.setView(R.layout.progress_layout)
        val progressDialog = builder.create()
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        progressDialog.show()

        val retroInstance = RetroInstance.getRetroInstance()
        val retroService = retroInstance.create(RetroServiceInterface::class.java)
        val call = retroService.getAllCountryFlagLang()
        call.enqueue(object: Callback<List<LanguageModel>>{
            override fun onResponse(p0: Call<List<LanguageModel>>, response: Response<List<LanguageModel>>) {
                Log.d(TAG, "response.body: ${response.body()}")
                liveDataList.postValue(response.body())
                progressDialog.dismiss()
            }

            override fun onFailure(p0: Call<List<LanguageModel>>, t: Throwable) {
                Log.d(TAG, "t: ${t.message}")
                liveDataList.postValue(null)
                progressDialog.dismiss()
            }
        })
    }
}