package com.example.mymultiplayer.view

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymultiplayer.R
import com.example.mymultiplayer.adapter.CountryListAdapter
import com.example.mymultiplayer.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = MainActivity::class.simpleName
        lateinit var mainActivity: Activity
        lateinit var mainFragmentManager: FragmentManager
    }

    lateinit var recyclerAdapter: CountryListAdapter
    var onSwipeTouchListener: OnSwipeTouchListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = this
        setContentView(R.layout.activity_main)
        mainFragmentManager = supportFragmentManager
        mainFragmentManager.commit {
            setReorderingAllowed(true)
            add<MainFragment>(R.id.fragment_container_view)
        }

        onSwipeTouchListener = OnSwipeTouchListener(this, findViewById(R.id.fragment_container_view))
        //initRecyclerView()
        //initViewModel()
    }

    private fun initRecyclerView() {
        val countryListRecyclerview = findViewById<RecyclerView>(R.id.countryListRecyclerview)
        countryListRecyclerview.layoutManager = LinearLayoutManager(this)
        recyclerAdapter = CountryListAdapter(this)
        countryListRecyclerview.adapter = recyclerAdapter
    }

    private fun initViewModel() {
        val viewModel: MainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.getLiveDataObserver().observe(this, Observer {
            if (it != null) {
                recyclerAdapter.setCountryList(it)
                recyclerAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "Error in getting list", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.makeAPICall()
    }

    class OnSwipeTouchListener internal constructor(context: Context, view: View) : View.OnTouchListener {
        private companion object {
            private const val swipeThreshold = 100
            private const val swipeVelocityThreshold = 100
        }

        private val gestureDetector: GestureDetector
        private var context: Context
        private var menuLeftFragment: MenuLeftFragment? = null

        init {
            gestureDetector = GestureDetector(context, GestureListener())
            view.setOnTouchListener(this)
            this.context = context
            menuLeftFragment = MenuLeftFragment()
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
            mainFragmentManager.beginTransaction().add(R.id.mainConstraintLayout, menuLeftFragment!!).addToBackStack(null).commit()
        }

        internal fun onSwipeLeft() {
            Log.d(TAG, "onSwipeLeft")
            mainFragmentManager.beginTransaction().remove(menuLeftFragment!!).commit()
        }

        internal fun onSwipeTop() {
            Log.d(TAG, "onSwipeTop")
        }

        internal fun onSwipeBottom() {
            Log.d(TAG, "onSwipeBottom")
        }
    }


}