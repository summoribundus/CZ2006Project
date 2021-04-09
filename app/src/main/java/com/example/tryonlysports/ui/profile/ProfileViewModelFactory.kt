package com.example.tryonlysports.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.IllegalArgumentException

/**
 * This is the Profile ViewModelFactory which instantiates and returns the ViewModel object that survives configuration changes of Profile fragment.
 *
 * @property db the firebase database.
 * @property userName the userName of the user.
 *
 * @author Liu Zhixuan, Li Rui
 */
class ProfileViewModelFactory(val db: FirebaseFirestore, val userName: String): ViewModelProvider.Factory {
    /**
     * Creates ViewModel for Profile Fragment and passes the database reference and user Email info.
     *
     * @param T ViewModel type.
     * @param modelClass general type of model class that we are going to assign.
     * @return the created ProfileViewModel given the username and firebase instance.
     */
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(db, userName) as T
        }
        throw IllegalArgumentException("Unknown view class")
    }
}
