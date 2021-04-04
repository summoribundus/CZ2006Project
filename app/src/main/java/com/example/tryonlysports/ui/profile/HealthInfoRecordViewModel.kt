package com.example.tryonlysports.ui.profile

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class HealthInfoRecordViewModel(val db: FirebaseFirestore, val userName: String): ViewModel()  {

    fun saveToFirestore(value: Int) {
        val healthInfo: CollectionReference = db.collection("healthInfo")


        val formattedDate: Timestamp = Timestamp.now()

        val history = HealthInfo(formattedDate, value, "Weight", userName)

        // Add a new document to the restaurants collection
        healthInfo.add(history)
    }

    inner class HealthInfo(val recordTime: Timestamp,
                           val value: Int,
                           val valueType: String,
                           val userName: String)
}