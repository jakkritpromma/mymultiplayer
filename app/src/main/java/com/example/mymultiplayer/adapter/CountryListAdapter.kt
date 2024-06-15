package com.example.mymultiplayer.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymultiplayer.R
import com.example.mymultiplayer.model.CountryModel


class CountryListAdapter(val activity: Activity) : RecyclerView.Adapter<CountryListAdapter.MyViewHolder>() {
    private var countryList: List<CountryModel>? = null


    fun setCountryList(countryList: List<CountryModel>?) {
        this.countryList = countryList
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): CountryListAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_list_row, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryListAdapter.MyViewHolder, position: Int) {
        holder.bind(countryList?.get(position)!!, activity)
    }

    override fun getItemCount(): Int {
        if(countryList == null)return 0
        else return countryList?.size!!
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val flagImage = view.findViewById<ImageView>(R.id.flagImage)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvLang = view.findViewById<TextView>(R.id.tvLang)

        fun bind(data: CountryModel, activity: Activity) {
            tvName.text = "aa"
            tvLang.text = "bb"
        }
    }

}