package com.example.tryonlysports.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

<<<<<<< Updated upstream
=======
/**
 * This is the ViewModel that stores and manages UI related data in the lifecycle of the profile fragment.
 *
 * @property db the firebase database.
 * @property name the user name to get information from firebase database.
 *
 * @author Liu Zhixuan, Li Rui
 */
>>>>>>> Stashed changes
class ProfileViewModel(val db: FirebaseFirestore, val name: String): ViewModel() {

    private val _nearest_whistory = MutableLiveData<WorkoutHistory>()
    val nearest_whistory : LiveData<WorkoutHistory> get() = _nearest_whistory

    private val _nearest_shistory = MutableLiveData<ScheduleHistory>()
    val nearest_shistory : LiveData<ScheduleHistory> get() = _nearest_shistory

    private val _nearest_hhistory = MutableLiveData<HealthInfoHistory>()
    val nearest_hhistory : LiveData<HealthInfoHistory> get() = _nearest_hhistory

    init {
        getData()
    }

    fun getData() {
        getNearestHealthHistoryFromFirebase()
        getNearestScheduleHistoryFromFirebase()
        getNearestWorkoutHistoryFromFirebase()
    }

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