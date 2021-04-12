package com.example.tryonlysports.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.IllegalArgumentException

/**
 * This is the HealthInfoRecord ViewModelFactory which instantiates and returns the ViewModel object that survives configuration changes of HealthInfoRecord fragment.
 *
 * @property db the firebase database.
 * @property userName the userName of the user.
 *
 * @author Li Rui, Liu Zhixuan
 */
class HealthInfoRecordViewModelFactory(val db: FirebaseFirestore, val userName: String): ViewModelProvider.Factory {
    /**
     * Creates ViewModel for Health Info Record Fragment and passes the database reference and user Email info.
     *
     * @param T ViewModel type.
     * @param modelClass general type of model class that we are going to assign.
     * @return the created HealthInfoRecordViewModel given the username and firebase instance.
     */
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HealthInfoRecordViewModel::class.java)) {
            return HealthInfoRecordViewModel(db, userName) as T
        }
        throw IllegalArgumentException("Unknown view class")
    }
}
