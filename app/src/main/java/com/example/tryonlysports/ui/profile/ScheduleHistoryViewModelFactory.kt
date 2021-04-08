package com.example.tryonlysports.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.IllegalArgumentException

/**
 * This is the ScheduleHistory ViewModelFactory which instantiates and returns the ViewModel object that survives configuration changes of ScheduleHistory fragment.
 *
 * @property db the firebase database.
 * @property userName the userName of the user.
 */
class ScheduleHistoryViewModelFactory (val db: FirebaseFirestore, val userName: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleHistoryViewModel::class.java)) {
            return ScheduleHistoryViewModel(db, userName) as T
        }
        throw IllegalArgumentException("Unknown view class")
    }
}