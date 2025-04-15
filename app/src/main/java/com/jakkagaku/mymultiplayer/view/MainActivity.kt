package com.jakkagaku.mymultiplayer.view

import android.Manifest
import android.Manifest.permission
import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.BLUETOOTH_SCAN
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.jakkagaku.mymultiplayer.R
import com.jakkagaku.mymultiplayer.util.CheckBluetooth
import com.jakkagaku.mymultiplayer.viewmodel.BtViewModel
import com.jakkagaku.mymultiplayer.viewmodel.TimeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity-"
    }

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var requireBtPermissions: ActivityResultLauncher<Array<String>>
    private var isBluetoothConnectGranted = false
    private var isBluetoothScanGranted = false
    private var isBackGroundLocationGranted = false
    private var isAccessCoarseLocationGranted = false
    private var isAccessFineLocationGranted = false
    private var isReadMediaVideoGranted = false
    private var isReadMediaAudioGranted = false
    private var isReadExternalStorageGranted = false
    private var onSwipeTouchListener: OnSwipeTouchListener? = null
    private val btViewModel: BtViewModel by viewModels()
    private val PERMISSION_REQUEST_CODE = 1
    private lateinit var auth: FirebaseAuth
    private var tvUpdatedTime: TextView? = null
    private lateinit var tvSignIn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideStatusBar()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        btViewModel.startUsingBy(this)
        onSwipeTouchListener = OnSwipeTouchListener(this, findViewById(R.id.fragment))
        val viewModel: TimeViewModel = ViewModelProvider(this).get(TimeViewModel::class.java)
        viewModel.liveData.observe(this) { it?.let { tvUpdatedTime?.text = it.time } }
        lifecycleScope.launch { TimeViewModel.startFlow(viewModel) }

        auth = FirebaseAuth.getInstance()

        tvUpdatedTime = findViewById(R.id.tvUpdatedTime)
        tvSignIn = findViewById(R.id.tv_sign_in)
        tvSignIn.setOnClickListener {
            startGoogleSignIn()
        }
    }

    override fun onStart() {
        super.onStart()
        permissionLauncher = registerForActivityResult(RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                val permission = it.key
                val result = it.value
                Log.d(TAG, "permission: ${permission} = ${result}")
                if (permission.contains("android.permission.ACCESS_FINE_LOCATION") && result) { //Starting from Android 10 (API 29),
                    // you need to request foreground permission (ACCESS_FINE_LOCATION) first,
                    // and background permission (ACCESS_BACKGROUND_LOCATION) separately if needed.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), PERMISSION_REQUEST_CODE)
                        }
                    }
                }
            }
        }
        requireBtPermissions = registerForActivityResult(RequestMultiplePermissions()) {
            fun checkPrm(p: String) = ContextCompat.checkSelfPermission(baseContext, p) == PackageManager.PERMISSION_GRANTED
            Log.d(TAG, "requireBtPermissions checkPrm(ACCESS_BACKGROUND_LOCATION): " + checkPrm(ACCESS_BACKGROUND_LOCATION))
            Log.d(TAG, "requireBtPermissions checkPrm(BLUETOOTH_SCAN): " + checkPrm(BLUETOOTH_SCAN))

            if (checkPrm(ACCESS_BACKGROUND_LOCATION) && checkPrm(BLUETOOTH_SCAN)) {
                Log.d(TAG, "requireBtPermissions if (checkPrm(ACCESS_BACKGROUND_LOCATION) && checkPrm(BLUETOOTH_SCAN)) {")
                CheckBluetooth.btPermitted = true
            } else {
                Log.e(TAG, "requireBtPermissions The application can't run without the required permissions")
            }
        }
        requestPermissions()

        //val currentUser = auth.currentUser
    }

    private fun requestPermissions() {
        isBluetoothConnectGranted = ContextCompat.checkSelfPermission(this, permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        Log.d(TAG, "isBluetoothConnectGranted: $isBluetoothConnectGranted")
        isBluetoothScanGranted = ContextCompat.checkSelfPermission(this, permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
        Log.d(TAG, "isBluetoothScanGranted: $isBluetoothScanGranted")
        isBackGroundLocationGranted = ContextCompat.checkSelfPermission(this, permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
        Log.d(TAG, "isBackGroundLocationGranted: $isBackGroundLocationGranted")
        isReadMediaVideoGranted = ContextCompat.checkSelfPermission(this, permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED
        Log.d(TAG, "isReadMediaVideoGranted: $isReadMediaVideoGranted")
        isReadMediaAudioGranted = ContextCompat.checkSelfPermission(this, permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
        Log.d(TAG, "isReadMediaAudioGranted: $isReadMediaAudioGranted")
        isReadExternalStorageGranted = ContextCompat.checkSelfPermission(this, permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        Log.d(TAG, "isReadExternalStorageGranted: $isReadExternalStorageGranted")
        isAccessCoarseLocationGranted = ContextCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        Log.d(TAG, "isAccessCoarseLocationGranted: $isAccessCoarseLocationGranted")
        isAccessFineLocationGranted = ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        Log.d(TAG, "isAccessFineLocationGranted: $isAccessFineLocationGranted")

        val permissionRequestList = ArrayList<String>()
        if (!isBluetoothConnectGranted) permissionRequestList.add(permission.BLUETOOTH_CONNECT)
        if (!isBluetoothScanGranted) permissionRequestList.add(permission.BLUETOOTH_SCAN)
        if (!isReadMediaVideoGranted) permissionRequestList.add(permission.READ_MEDIA_VIDEO)
        if (!isReadMediaAudioGranted) permissionRequestList.add(permission.READ_MEDIA_AUDIO)
        if (!isAccessCoarseLocationGranted) permissionRequestList.add(permission.ACCESS_COARSE_LOCATION)
        if (!isAccessFineLocationGranted) permissionRequestList.add(permission.ACCESS_FINE_LOCATION)

        Log.d(TAG, "android.os.Build.VERSION.SDK_INT: " + android.os.Build.VERSION.SDK_INT) //if(android.os.Build.VERSION.SDK_INT >= android.os.)
        // TODO android 12 or lower if (!isReadExternalStorageGranted) permissionRequestList.add(permission.READ_EXTERNAL_STORAGE)

        if (permissionRequestList.isNotEmpty()) {
            Log.d(TAG, "permissionRequestList.size: " + permissionRequestList.size)
            permissionLauncher.launch(permissionRequestList.toTypedArray())
        } else {
            Log.d(TAG, "else empty")
            CheckBluetooth(this) { btViewModel.btAvailable = true }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun hideStatusBar() {
        @Suppress("DEPRECATION") if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        hideStatusBar()
        return super.onTouchEvent(event)
    }

    class OnSwipeTouchListener internal constructor(context: Context, view: View) : View.OnTouchListener {
        private companion object {
            private const val swipeThreshold = 100
            private const val swipeVelocityThreshold = 100
        }

        private val gestureDetector: GestureDetector
        private var context: Context

        init {
            gestureDetector = GestureDetector(context, GestureListener())
            view.setOnTouchListener(this)
            this.context = context
        }

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            return gestureDetector.onTouchEvent(event)
        }

        inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                var result = false
                try {
                    Log.d(TAG, "e1.x: " + e1!!.x + " e1.y: " + e1.y)
                    Log.d(TAG, "e2.x: " + e2.x + " e2.y: " + e2.y)
                    Log.d(TAG, "velocityX: $velocityX + velocityY: $velocityY")
                    val diffY = e2.y - e1.y
                    val diffX = e2.x - e1.x
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > swipeThreshold && Math.abs(velocityX) > swipeVelocityThreshold) {
                            if (diffX > 0) {
                                onSwipeRight()
                            } else {
                                onSwipeLeft()
                            }
                            result = true
                        }
                    } else if (Math.abs(diffY) > swipeThreshold && Math.abs(velocityY) > swipeVelocityThreshold) {
                        if (diffY > 0) {
                            onSwipeBottom()
                        } else {
                            onSwipeTop()
                        }
                        result = true
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e(TAG, "e: $e")
                }
                return result
            }
        }

        internal fun onSwipeRight() {
            Log.d(TAG, "onSwipeRight")
            Navigation.findNavController(context as Activity, R.id.fragment).navigate(R.id.action_settingFragment_to_mainFragment)
        }

        internal fun onSwipeLeft() {
            Log.d(TAG, "onSwipeLeft")
            Navigation.findNavController(context as Activity, R.id.fragment).navigate(R.id.action_mainFragment_to_settingFragment)
        }

        internal fun onSwipeTop() {
            Log.d(TAG, "onSwipeTop")
        }

        internal fun onSwipeBottom() {
            Log.d(TAG, "onSwipeBottom")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        btViewModel.stopUsingBy(this)
    }

    private fun startGoogleSignIn() {
        val googleIdOption = GetGoogleIdOption.Builder().setServerClientId(getString(R.string.default_web_client_id)).setFilterByAuthorizedAccounts(false).build()
        val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
        val credentialManager = CredentialManager.create(this)
        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(request = request, context = this@MainActivity)
                val credential = result.credential
                handleSignIn(credential)
            } catch (e: GetCredentialException) {
                Log.e(TAG, "Google Sign-In failed", e)
            }
        }
    }

    private fun handleSignIn(credential: Credential) {
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val credential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Toast.makeText(this, "Signed in with " + user?.email, Toast.LENGTH_LONG).show();
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Failed to sign in.", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Log.w(TAG, "Credential is not of type Google ID!")
        }
    }
}