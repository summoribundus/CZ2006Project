package com.example.tryonlysports.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tryonlysports.MainActivity
import com.example.tryonlysports.R
import com.example.tryonlysports.databinding.FragmentWeatherBinding


class WeatherFragment : Fragment() {

    private lateinit var binding: FragmentWeatherBinding


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_weather, container, false
        )

        val application = requireNotNull(this.activity).application

        val mainActivity: MainActivity = activity as MainActivity
        val location = mainActivity.currentLocation
        val viewModelFactory = WeatherViewModelFactory(location, application)

        val viewModel = ViewModelProvider(this, viewModelFactory).get(WeatherViewModel::class.java)
        binding.weatherViewModel = viewModel

        binding.setLifecycleOwner (this)

        return binding.root
    }




}