package com.example.tryonlysports.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.IllegalArgumentException

class PersonalInfoViewModelFactory (val db: FirebaseFirestore, val emailID: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonalInfoViewModel::class.java)) {
            return PersonalInfoViewModel(db, emailID) as T
        }
        throw IllegalArgumentException("Unknown view class")
    }
}