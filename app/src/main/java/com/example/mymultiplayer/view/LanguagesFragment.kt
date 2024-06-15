package com.example.mymultiplayer.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymultiplayer.R
import com.example.mymultiplayer.adapter.CountryListAdapter
import com.example.mymultiplayer.databinding.FragmentLanguagesBinding
import com.example.mymultiplayer.viewmodel.MainActivityViewModel

class LanguagesFragment : Fragment() {
    private val TAG = LanguagesFragment::class.simpleName
    private var binding: FragmentLanguagesBinding? = null
    private lateinit var recyclerAdapter: CountryListAdapter

    @SuppressLint("NotifyDataSetChanged") override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        binding = FragmentLanguagesBinding.inflate(inflater, container, false)
        val view = binding?.root

        val countryListRecyclerview = view?.findViewById<RecyclerView>(R.id.countryListRecyclerview)
        countryListRecyclerview?.layoutManager = LinearLayoutManager(MainActivity.mainActivity)
        recyclerAdapter = CountryListAdapter(MainActivity.mainActivity)
        countryListRecyclerview?.adapter = recyclerAdapter

        val viewModel: MainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.getLiveDataObserver().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                recyclerAdapter.setCountryList(it)
                recyclerAdapter.notifyDataSetChanged()
            } else {
                MainActivity.mainActivity.runOnUiThread(Runnable {
                    Toast.makeText(MainActivity.mainActivity, "Error in getting list", Toast.LENGTH_SHORT).show()
                })
            }
        })
        viewModel.makeAPICall()
        return view
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        super.onDestroyView()
        binding = null
    }
}