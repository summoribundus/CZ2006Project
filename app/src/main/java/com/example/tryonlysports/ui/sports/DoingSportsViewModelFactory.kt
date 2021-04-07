package com.example.tryonlysports.ui.sports

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

/**
 * This is the Doing Sports ViewModelFactory which instantiates and returns the ViewModel object that survives configuration changes of Doing Sports fragment.
 *
 * @property type the type of workout (e.g. cycling/jogging/walking)
 */
class DoingSportsViewModelFactory(val type:String): ViewModelProvider.Factory {
    /**
     * Creates ViewModel for Doing Sports Fragment and passes the type.
     *
     * @param T ViewModel type.
     * @param modelClass general type of model class that we are going to assign.
     * @return the created DoingSportsViewModel given the workout type.
     */
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DoingSportsViewModel::class.java)) {
            return DoingSportsViewModel(type) as T
        }
        throw IllegalArgumentException("Unknown view class")
    }


}