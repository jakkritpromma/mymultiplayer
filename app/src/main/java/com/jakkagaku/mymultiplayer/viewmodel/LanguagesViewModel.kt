package com.jakkagaku.mymultiplayer.viewmodel

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.WindowManager
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.jakkagaku.mymultiplayer.R
import com.jakkagaku.mymultiplayer.model.LanguageModel
import com.jakkagaku.mymultiplayer.retrofit.RetroInstance
import com.jakkagaku.mymultiplayer.retrofit.RetroServiceInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LanguagesViewModel : ViewModel() {
    private val TAG = LanguagesViewModel::class.simpleName
    private var _liveDataList = mutableStateListOf<LanguageModel>()
    val itemList: SnapshotStateList<LanguageModel> get() = _liveDataList

    fun makeAPICall(fragmentActivity: FragmentActivity) {
        Log.d(TAG, "makeAPICall")
        val builder = AlertDialog.Builder(fragmentActivity)
        builder.setView(R.layout.progress_layout)
        val progressDialog = builder.create()
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        progressDialog.setCancelable(false)
        progressDialog.show()

        val retroInstance = RetroInstance.getRetroInstance()
        val retroService = retroInstance.create(RetroServiceInterface::class.java)
        val call = retroService.getAllCountryFlagLang()
        call.enqueue(object: Callback<List<LanguageModel>>{
            override fun onResponse(p0: Call<List<LanguageModel>>, response: Response<List<LanguageModel>>) {
                Log.d(TAG, "response.body: ${response.body()}")
                val respondedList = response.body()
                for (i in 0 until respondedList!!.size) {
                    _liveDataList.add(respondedList.get(i))
                }
                progressDialog.dismiss()
            }

            override fun onFailure(p0: Call<List<LanguageModel>>, t: Throwable) {
                Log.d(TAG, "t: ${t.message}")
                _liveDataList.clear()
                progressDialog.dismiss()
            }
        })
    }
}