package com.example.tryonlysports.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tryonlysports.ui.sports.DoingSportsViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.IllegalArgumentException

/**
 * This is the WorkoutHistory ViewModelFactory which instantiates and returns the ViewModel object that survives configuration changes of WorkoutHistory fragment.
 *
 * @property db the firebase database.
 * @property userName the userName of the user.
 */
class WorkoutHistoryViewModelFactory(val db: FirebaseFirestore, val userName: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutHistoryViewModel::class.java)) {
            return WorkoutHistoryViewModel(db, userName) as T
        }
        throw IllegalArgumentException("Unknown view class")
    }
}