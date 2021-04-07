package com.example.tryonlysports.ui.weather

import android.app.Application
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Weather ViewModel Factory Class
 *
 * This class construct view model and pass in parameters needed.
 *
 * @property location The user's current location
 * @property application The application this view is running
 * @author Li Rui
 */
class WeatherViewModelFactory (private val location: Location,
                               private val application: Application) : ViewModelProvider.AndroidViewModelFactory(application) {
    /**
     * Create the view model class
     *
     * @param T view model
     * @param modelClass view model class
     * @return T an instance of view model
     */
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(application, location) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}