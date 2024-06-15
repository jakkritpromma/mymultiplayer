package com.example.mymultiplayer.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.mymultiplayer.R

class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = MainActivity::class.simpleName
        @SuppressLint("StaticFieldLeak") lateinit var mainActivity: Activity
        lateinit var mainFragmentManager: FragmentManager
    }

    var onSwipeTouchListener: OnSwipeTouchListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        mainActivity = this
        setContentView(R.layout.activity_main)
        hideStatusBar()
        mainFragmentManager = supportFragmentManager
        mainFragmentManager.commit {
            setReorderingAllowed(true)
            add<MainFragment>(R.id.fragment_container_view)
        }
        onSwipeTouchListener = OnSwipeTouchListener(this, findViewById(R.id.fragment_container_view))
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
        private var settingFragment: SettingFragment? = null

        init {
            gestureDetector = GestureDetector(context, GestureListener())
            view.setOnTouchListener(this)
            this.context = context
            settingFragment = SettingFragment()
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
            mainFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in, // enter
                R.anim.slide_out, // exit
                R.anim.fade_in, // popEnter
                R.anim.slide_out // popExit
            ).remove(settingFragment!!).commit()
        }

        internal fun onSwipeLeft() {
            Log.d(TAG, "onSwipeLeft")
            mainFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in, // enter
                R.anim.slide_out, // exit
                R.anim.fade_in, // popEnter
                R.anim.slide_out // popExit
            ).replace(R.id.mainConstraintLayout, settingFragment!!).addToBackStack(null).commit()

        }

        internal fun onSwipeTop() {
            Log.d(TAG, "onSwipeTop")
        }

        internal fun onSwipeBottom() {
            Log.d(TAG, "onSwipeBottom")
        }
    }
}