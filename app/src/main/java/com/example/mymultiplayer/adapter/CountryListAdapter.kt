package com.example.mymultiplayer.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymultiplayer.R
import com.example.mymultiplayer.model.CountryModel
import com.squareup.picasso.Picasso


class CountryListAdapter(val activity: Activity) : RecyclerView.Adapter<CountryListAdapter.MyViewHolder>() {

    companion object {
        private val TAG = CountryListAdapter::class.qualifiedName
    }

    private var countryList: List<CountryModel>? = null

    fun setCountryList(countryList: List<CountryModel>?) {
        this.countryList = countryList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryListAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_list_row, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryListAdapter.MyViewHolder, position: Int) {
        holder.bind(countryList?.get(position)!!, activity)
    }

    override fun getItemCount(): Int {
        if (countryList == null) return 0
        else return countryList?.size!!
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val flagImage = view.findViewById<ImageView>(R.id.flagImage)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvLang = view.findViewById<TextView>(R.id.tvLang)

        fun bind(data: CountryModel, activity: Activity) {
            val mutableSetFlags: MutableSet<String>? = data.flags?.keySet()
            if(mutableSetFlags != null) {
                Log.d(TAG, "mutableSetFlags.toList().get(0): " + mutableSetFlags.toList().get(0))
                val firstKey = mutableSetFlags.toList().get(0)
                val firstValue = data.flags.get(firstKey).toString()
                val firstImgLink = firstValue.substring(1, firstValue.length - 1)
                Log.d(TAG, "firstImgLink: $firstImgLink")
                Picasso.get().load(firstImgLink).into(flagImage);
            }

            val commonName = data.name?.get("common").toString()
            tvName.text = commonName.substring(1, commonName.length - 1)

            val mutableSetLang: MutableSet<String>? = data.languages?.keySet()
            if (mutableSetLang != null) {
                Log.d(TAG, "mutableSet.toList().get(0): " + mutableSetLang.toList().get(0))
                val firstKey = mutableSetLang.toList().get(0)
                val firstValue = data.languages.get(firstKey).toString()
                val language = firstValue.substring(1, firstValue.length - 1)
                Log.d(TAG, "language: $language")
                tvLang.text = language
            }
        }
    }

}
