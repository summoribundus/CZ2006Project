package com.example.tryonlysports.ui.profile

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

/**
 * This is the ViewModel that stores and manages UI related data in the lifecycle of the Health Info Record fragment.
 *
 * @property db the firebase database.
 * @property userName the userName to get information from firebase database.
 */
class HealthInfoRecordViewModel(val db: FirebaseFirestore, val userName: String): ViewModel()  {
    /**
     * Saves the health info record to Firebase data store.
     *
     * @param value the value of the health info that user inputs
     */
    fun saveToFirestore(value: Int) {
        val healthInfo: CollectionReference = db.collection("healthInfo")


        val formattedDate: Timestamp = Timestamp.now()

        val history = HealthInfo(formattedDate, value, "Weight", userName)

        // Add a new document to the restaurants collection
        healthInfo.add(history)
    }

    /**
     * The data model class to save to firebase as a HealthInfo object.
     *
     * @property recordTime date and time that the health info is recorded.
     * @property value value of the recorded health info (e.g. '50", the value of the recorded weight).
     * @property valueType type of the recorded health info (e.g. "Weight").
     * @property userName username of the user that the record belongs to.
     */
    inner class HealthInfo(val recordTime: Timestamp,
                           val value: Int,
                           val valueType: String,
                           val userName: String)
}