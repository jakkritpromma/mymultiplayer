package com.jakkagaku.mymultiplayer.util

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.ContextCompat.checkSelfPermission
import com.jakkagaku.mymultiplayer.view.MainActivity

class CheckBluetooth(private val activity: MainActivity, private val onSuccess: () -> Unit) {
    companion object{
        var btPermitted = false
        var btEnabled = false
    }
    private val TAG = CheckBluetooth::class.simpleName
    private lateinit var bluetoothAdapter: BluetoothAdapter

    private var btPresent = false

    val requireBtEnabling = activity.registerForActivityResult(StartActivityForResult().apply {
        Log.d(TAG, "requireBtEnabling")
        createIntent(activity.baseContext, Intent(ACTION_REQUEST_ENABLE))
    }) {
        if (it.resultCode == RESULT_OK) {
            Log.d(TAG, "requireBtEnabling if (it.resultCode == RESULT_OK) {")
            btEnabled = true
            //go()
        } else {
            Log.e(TAG, "requireBtEnabling This application can not work with Bluetooth disabled")
        }
    }

    init {
        Log.d(TAG, "init")
        go()
    }

    private fun go() {
        Log.d(TAG, "go")
        if ((btPresent || checkBTisPresent()).also { btPresent = it } && (btPermitted || checkBtPermissions()).also { btPermitted = it }) {
            Log.d(TAG, "if ((btPresent || checkBTisPresent()).also { btPresent ...")
            btEnabled = checkEnabledOrTryToEnableBT()
            onSuccess()
        }
    }

    fun checkBTisPresent(): Boolean {
        Log.d(TAG, "checkBTisPresent()")
        val ba = BluetoothAdapter.getDefaultAdapter()
        return if (ba != null) {
            Log.d(TAG, "checkBTisPresent()  return if (ba != null) {")
            this.bluetoothAdapter = ba
            true
        } else {
            Log.e(TAG, "Missing BluetoothAdapter in this device!")
            false
        }
    }

    private fun checkEnabledOrTryToEnableBT(): Boolean {
        Log.d(TAG, " checkEnabledOrTryToEnableBT")
        return if (bluetoothAdapter.isEnabled) {
            Log.d(TAG, " checkEnabledOrTryToEnableBT return if (bluetoothAdapter.isEnabled) {")
            true
        } else {
            requireBtEnabling.launch(Intent(ACTION_REQUEST_ENABLE))
            Log.d(TAG, " checkEnabledOrTryToEnableBT else")
            false
        }
    }

    private fun checkBtPermissions(): Boolean {
        Log.d(TAG, "checkBtPermissions")
        fun checkPrm(p: String) = checkSelfPermission(activity.baseContext, p) == PERMISSION_GRANTED
        Log.d(TAG, "checkPrm(ACCESS_FINE_LOCATION): " + checkPrm(ACCESS_FINE_LOCATION))
        Log.d(TAG, "checkPrm(ACCESS_BACKGROUND_LOCATION): " + checkPrm(ACCESS_BACKGROUND_LOCATION))
        return if (checkPrm(ACCESS_FINE_LOCATION) && checkPrm(ACCESS_BACKGROUND_LOCATION)) {
            true
        } else {
            //requireBtPermissions.launch(arrayOf(ACCESS_FINE_LOCATION, ACCESS_BACKGROUND_LOCATION))
            false
        }
    }


}