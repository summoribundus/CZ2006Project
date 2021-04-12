package com.example.tryonlysports.ui.facility

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * This is the ViewModel that stores and manages UI related data in the lifecycle of the Sports Facility fragment.
 *
 * @author Li Rui, Ye Ziyuan, Wan Qian
 */
class SportsFacilityViewModel : ViewModel() {

    /**
     * The information as mutable live private data.
     */
    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }

    /**
     * The information as live data.
     */
    val text: LiveData<String> = _text
}
