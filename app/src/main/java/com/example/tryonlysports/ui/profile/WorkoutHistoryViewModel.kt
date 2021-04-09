package com.example.tryonlysports.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

/**
 * This is the ViewModel that stores and manages UI related data in the lifecycle of the workout history fragment.
 *
 * @property db the firebase database.
 * @property userName the user name to get information from firebase database.
 *
 * @author Liu Zhixuan, Li Rui
 */
class WorkoutHistoryViewModel(val db: FirebaseFirestore, val userName: String): ViewModel() {
    /**
     * The workoutHistory as mutable live private data.
     */
    private val _whistory = MutableLiveData<MutableList<WorkoutHistory>>()

    /**
     * The workoutHistory as live data.
     */
    val whistory : LiveData<MutableList<WorkoutHistory>> get() = _whistory

    /**
     * Will be called when iniitialized.
     */
    init {
        getAllWorkoutHistoryFromFirebase()
    }

    /**
     * Get all of the workout histories of the user from firebase data store.
     *
     */
    fun getAllWorkoutHistoryFromFirebase(){
        var list: MutableList<WorkoutHistory> = mutableListOf()
        db.collection("workoutHistory").whereEqualTo("userName", userName).orderBy("date", Query.Direction.DESCENDING).get().addOnSuccessListener { documents ->
            for (doc in documents) {
                val wh: WorkoutHistory = doc.toObject<WorkoutHistory>()
                wh.id = doc.id
                list.add(wh)
                Log.i("WorkoutHistory", wh.toString())
            }
            _whistory.value = list
        }.addOnFailureListener {
            Log.d("workoutDAO", "Why failed?")
        }
    }


}
