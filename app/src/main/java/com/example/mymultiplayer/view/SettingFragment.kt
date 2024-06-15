package com.example.mymultiplayer.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.Fragment
import com.example.mymultiplayer.R
import com.example.mymultiplayer.databinding.FragmentSetingBinding

class SettingFragment : Fragment() {
    private val TAG = SettingFragment::class.simpleName
    private var binding: FragmentSetingBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        binding = FragmentSetingBinding.inflate(inflater, container, false)
        val view = binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        val languagesFragment = LanguagesFragment()
        binding?.tvLanguages?.setOnClickListener {
            Toast.makeText(context, "click",  LENGTH_LONG).show()
            MainActivity.mainFragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in, // enter
                R.anim.fade_out, // exit
                R.anim.fade_in, // popEnter
                R.anim.fade_out // popExit
            ).add(R.id.mainConstraintLayout, languagesFragment).addToBackStack(null).commit()
        }
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        super.onDestroyView()
        binding = null
    }
}