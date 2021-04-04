package com.example.tryonlysports.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tryonlysports.ui.sports.DoingSportsViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.IllegalArgumentException

class WorkoutHistoryViewModelFactory(val db: FirebaseFirestore, val userName: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutHistoryViewModel::class.java)) {
            return WorkoutHistoryViewModel(db, userName) as T
        }
        throw IllegalArgumentException("Unknown view class")
    }
}