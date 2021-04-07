package com.example.tryonlysports.ui.sports

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.Polyline
import com.google.maps.android.SphericalUtil
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.floor

/**
 * This is the ViewModel that stores and manages UI related data in the lifecycle of the Doing Sports fragment.
 *
 * @property type type of the workout (e.g. cycling/jogging/walking)
 *
 * @author Ye Ziyuan
 */
class DoingSportsViewModel(val type:String): ViewModel() {
    private val _typeStr =  MutableLiveData<String>()
    val typeStr : LiveData<String> get() = _typeStr

    private val _startTime = MutableLiveData<String>()
    val startTime : LiveData<String> get() = _startTime

    private val _timePassed = MutableLiveData<String>()
    val timePassed : LiveData<String> get() = _timePassed

    private val _speed = MutableLiveData<String>()
    val speed : LiveData<String> get() = _speed

    private val _totalDistance = MutableLiveData<String>()
    val totalDistance : LiveData<String> get() = _totalDistance

    private val _calories = MutableLiveData<Double>()
    val calories : LiveData<Double> get() = _calories

    private val _passedTimeValue = MutableLiveData<Long>()
    val passedTimeValue: LiveData<Long> get() = _passedTimeValue

    private val _totalDistanceValue = MutableLiveData<Double>()
    val totalDistanceValue : LiveData<Double> get() = _totalDistanceValue

    private var tStart = System.currentTimeMillis()

    init {
        _typeStr.value = type
        Log.i("SportsViewModel", _typeStr.value.toString())
        initProperty()
    }

    /**
     * Manages the initial properties, Record start time & speed & total distance.
     *
     */
    private fun initProperty() {
        _startTime.value = LocalDateTime.now().toString()
        updateTimePassed()
        _speed.value= "0 mph"
        _totalDistance.value = "0 km"
    }

    /**
     * Updates the sports duration by retrieving the current time and comparing it with the start time.
     *
     */
    fun updateTimePassed() {
        val tPassed: Long = System.currentTimeMillis() - tStart
        _passedTimeValue.value = tPassed
        _timePassed.value = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(tPassed),
            TimeUnit.MILLISECONDS.toSeconds(tPassed) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(tPassed)))
    }

    /**
     * Updates the real time speed display.
     *
     * @param location the current location of the user given by android platform.
     */
    fun updateSpeed(location: Location) {
        val speed = floor(location.speed * 2.23694)
        _speed.value = String.format("%.0f mph", speed)
    }

    /**
     * Calculates the spherical route length and updates the display.
     *
     * @param route the Polyline that draws the user's sports route.
     */
    fun updateRouteLength(route: Polyline){
        val totLength = SphericalUtil.computeLength(route.points)
        val distance = totLength/1000
        _totalDistanceValue.value = distance
        _totalDistance.value = String.format("%.3f km", distance)
        //divided by 1000 to get the distance in km
    }

}