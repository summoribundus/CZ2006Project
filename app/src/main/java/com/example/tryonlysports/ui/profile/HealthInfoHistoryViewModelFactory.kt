package com.example.tryonlysports.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.IllegalArgumentException

/**
 * This is the HealthInfoHistory ViewModelFactory which instantiates and returns the ViewModel object that survives configuration changes of HealthInfoHistory fragment.
 *
 * @property db the firebase database.
 * @property userName the userName of the user.
 */
class HealthInfoHistoryViewModelFactory (val db: FirebaseFirestore, val userName: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HealthInfoHistoryViewModel::class.java)) {
            return HealthInfoHistoryViewModel(db, userName) as T
        }
        throw IllegalArgumentException("Unknown view class")
    }
}