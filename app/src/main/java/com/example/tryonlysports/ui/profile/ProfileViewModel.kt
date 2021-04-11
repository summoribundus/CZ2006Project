package com.example.tryonlysports.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

/**
 * This is the ViewModel that stores and manages UI related data in the lifecycle of the profile fragment.
 *
 * @property db the firebase database.
 * @property name the user name to get information from firebase database.
 *
 * @author Li Rui, Liu Zhixuan
 */
class ProfileViewModel(val db: FirebaseFirestore, val name: String): ViewModel() {
    /**
     * The workoutHistory as mutable live private data.
     */
    private val _nearest_whistory = MutableLiveData<WorkoutHistory>()
    /**
     * The workoutHistory as live data.
     */
    val nearest_whistory : LiveData<WorkoutHistory> get() = _nearest_whistory

    /**
     * The scheduleHistory as mutable live private data.
     */
    private val _nearest_shistory = MutableLiveData<ScheduleHistory>()
    /**
     * The scheduleHistory as live data.
     */
    val nearest_shistory : LiveData<ScheduleHistory> get() = _nearest_shistory

    /**
     * The healthInfoHistory as mutable live private data.
     */
    private val _nearest_hhistory = MutableLiveData<HealthInfoHistory>()
    /**
     * The healthInfoHistory as live data.
     */
    val nearest_hhistory : LiveData<HealthInfoHistory> get() = _nearest_hhistory

    /**
     * Will be called when initialized.
     */
    init {
        getData()
    }

    /**
     * Calls the functions to get nearest histories of healthInfo, schedule and workout records.
     *
     */
    fun getData() {
        getNearestHealthHistoryFromFirebase()
        getNearestScheduleHistoryFromFirebase()
        getNearestWorkoutHistoryFromFirebase()
    }

    /**
     * Get the nearest workout record from firebase data store
     *
     */
    fun getNearestWorkoutHistoryFromFirebase(){
        db.collection("workoutHistory").whereEqualTo("userName", name).orderBy("date").limit(1).get().addOnSuccessListener { documents ->
            for (doc in documents) {
                val wh: WorkoutHistory = doc.toObject()
                wh.id = doc.id
                _nearest_whistory.value = wh
                Log.i("NearestWorkoutHistory", wh.toString())
            }
        }.addOnFailureListener {
            Log.d("nearest_workoutDAO", "Why failed?")
        }
    }
    /**
     * Get the nearest schedule record from firebase data store
     *
     */
    fun getNearestScheduleHistoryFromFirebase(){
        db.collection("schedule").whereEqualTo("userName", name).orderBy("startDateTime").limit(1).get().addOnSuccessListener { documents ->
            for (doc in documents) {
                val sh: ScheduleHistory = doc.toObject()
                sh.id = doc.id
                _nearest_shistory.value = sh
                Log.i("NearestScheduleHistory", sh.toString())
            }
        }.addOnFailureListener {
            Log.d("nearest_ScheduleDAO", "Why failed?")
        }
    }
    /**
     * Get the nearest health info record from firebase data store
     *
     */
    fun getNearestHealthHistoryFromFirebase(){
        db.collection("healthInfo").whereEqualTo("userName", name).orderBy("recordTime").limit(1).get().addOnSuccessListener { documents ->
            for (doc in documents) {
                val hh: HealthInfoHistory = doc.toObject()
                hh.id = doc.id
                _nearest_hhistory.value = hh
                Log.i("NearestHealthHistory", hh.toString())
            }
        }.addOnFailureListener {
            Log.d("nearest_HealthDAO", "Why failed?")
        }
    }

}
