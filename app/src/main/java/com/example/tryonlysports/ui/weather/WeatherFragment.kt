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

/**
 * The class controlling the inflate of weather fragment xml file
 * @author Li Rui
 */
class WeatherFragment : Fragment() {

    /**
     * The weather fragment data binding.
     */
    private lateinit var binding: FragmentWeatherBinding

    /**
     * The function called at view creation time.
     *
     * Inflate the weather fragment layout and create view model.
     *
     * @param inflater The layout inflater passed automatically.
     * @param container The container passed in automatically.
     * @param savedInstanceState The resources passed in when creating the view.
     * @return The view created by this function.
     */
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