package com.example.tryonlysports.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

class WorkoutHistoryViewModel(val db: FirebaseFirestore, val userName: String): ViewModel() {
    private val _whistory = MutableLiveData<MutableList<WorkoutHistory>>()
    val whistory : LiveData<MutableList<WorkoutHistory>> get() = _whistory

    init {
        getAllWorkoutHistoryFromFirebase()
    }

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