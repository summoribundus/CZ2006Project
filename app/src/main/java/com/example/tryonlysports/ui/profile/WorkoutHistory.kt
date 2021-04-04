package com.example.tryonlysports.ui.profile

import com.google.firebase.Timestamp

data class WorkoutHistory(val date: Timestamp?=null,
                          val avgSpeed: String?=null,
                          val duration: String?=null,
                          val userName: String?=null,
                          val distance: String?=null,
                          val calories: String?=null,
                        val workoutType: String?=null){
    lateinit var id: String
}
