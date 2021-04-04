package com.example.tryonlysports.ui.sports

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class DoingSportsViewModelFactory(val type:String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DoingSportsViewModel::class.java)) {
            return DoingSportsViewModel(type) as T
        }
        throw IllegalArgumentException("Unknown view class")
    }


}