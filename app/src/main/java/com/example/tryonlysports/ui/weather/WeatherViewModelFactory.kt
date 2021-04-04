package com.example.tryonlysports.ui.weather

import android.app.Application
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WeatherViewModelFactory (private val location: Location,
                               private val application: Application) : ViewModelProvider.AndroidViewModelFactory(application) {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(application, location) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}