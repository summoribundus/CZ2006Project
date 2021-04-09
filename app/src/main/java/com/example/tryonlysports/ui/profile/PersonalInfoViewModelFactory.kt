package com.example.tryonlysports.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.IllegalArgumentException

/**
 * This is the PersonalInfoHistory ViewModelFactory which instantiates and returns the ViewModel object that survives configuration changes of PersonalInfoHistory fragment.
 *
 * @property db the firebase database.
 * @property emailID the emailID of the user.
 *
 * @author Liu Zhixuan, Li Rui
 */
class PersonalInfoViewModelFactory (val db: FirebaseFirestore, val emailID: String): ViewModelProvider.Factory {
    /**
     * Creates ViewModel for Personal Info Fragment and passes the database reference and user Email info.
     *
     * @param T ViewModel type.
     * @param modelClass general type of model class that we are going to assign.
     * @return the created PersonalInfoRecordViewModel given the useremail and firebase instance.
     */
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonalInfoViewModel::class.java)) {
            return PersonalInfoViewModel(db, emailID) as T
        }
        throw IllegalArgumentException("Unknown view class")
    }
}
