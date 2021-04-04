package com.example.tryonlysports.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.IllegalArgumentException

class HealthInfoRecordViewModelFactory(val db: FirebaseFirestore, val userName: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HealthInfoRecordViewModel::class.java)) {
            return HealthInfoRecordViewModel(db, userName) as T
        }
        throw IllegalArgumentException("Unknown view class")
    }
}