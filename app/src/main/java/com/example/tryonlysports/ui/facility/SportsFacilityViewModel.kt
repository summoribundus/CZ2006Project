package com.example.tryonlysports.ui.facility

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
/** This view model stores UI data used in the SportsFacilityFragment
 *
 * @property _text contains data of the Sport Facility Information
 * @property text displays information of sports facility
 *
 * @author Wan Qian
 */
class SportsFacilityViewModel : ViewModel() {
    /**
     * The information of sports facility string as mutable live private data.
     */

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    /**
     * Information of Sports Facility string as live data.
     */
    val text: LiveData<String> = _text
}