package com.example.mymultiplayer.view

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
import com.example.mymultiplayer.databinding.FragmentLeftMenuBinding
import com.example.mymultiplayer.viewmodel.MainActivityViewModel

class MenuLeftFragment : Fragment() {
    private val TAG = MenuLeftFragment::class.simpleName
    private var _binding: FragmentLeftMenuBinding? = null
    lateinit var recyclerAdapter: CountryListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        _binding = FragmentLeftMenuBinding.inflate(inflater, container, false)
        val view = _binding?.root

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
        _binding = null
    }
}