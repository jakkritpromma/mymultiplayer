package com.jakkagaku.mymultiplayer.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jakkagaku.mymultiplayer.model.TimeModel
import com.jakkagaku.mymultiplayer.view.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.apache.commons.net.ntp.NTPUDPClient
import org.apache.commons.net.ntp.TimeInfo
import java.net.InetAddress
import java.text.SimpleDateFormat
import java.util.Date

class TimeViewModel : ViewModel() {
    companion object {
        private val TAG = TimeViewModel::class.simpleName
        suspend fun startFlow(viewModel: TimeViewModel) {
            val latestTime = flow {
                while (true) {
                    val time = viewModel.getTime()
                    emit(time)
                    Log.d(TAG, "delay(1000L) time: $time")
                    delay(1000L)
                }
            }
            latestTime.collect { value -> viewModel.liveData.postValue(TimeModel(value)) }
        }
    }

    var liveData: MutableLiveData<TimeModel> = MutableLiveData()

    fun getLiveDataObserver(): MutableLiveData<TimeModel> {
        return liveData
    }

    suspend fun getTime(): String {
        return withContext(Dispatchers.IO) {
            val sDateFormat = SimpleDateFormat("HH:mm:ss")
            try {
                val timeServer = "th.pool.ntp.org"
                val timeClient: NTPUDPClient = NTPUDPClient()
                val inetAddress = InetAddress.getByName(timeServer)
                val timeInfo: TimeInfo = timeClient.getTime(inetAddress)
                val returnTime = timeInfo.message.transmitTimeStamp.time
                val serverDateTime = Date(returnTime)
                val fServerTime = sDateFormat.format(serverDateTime.time)
                liveData.postValue(TimeModel(fServerTime))
                return@withContext fServerTime

            } catch (e: Exception) {
                Log.e(MainActivity.TAG, "within exception$e")
                val thisDate = Date()
                val fDate = sDateFormat.format(thisDate)
                liveData.postValue(TimeModel(fDate))
                return@withContext fDate
            }
        }
    }
}