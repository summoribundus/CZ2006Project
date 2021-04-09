package com.example.tryonlysports.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

<<<<<<< Updated upstream
=======
/**
 * This is the ViewModel that stores and manages UI related data in the lifecycle of the Personal Info fragment.
 *
 * @property db the firebase database.
 * @property emailID emailID of the user.
 *
 * @author Liu Zhixuan, Li Rui
 */
>>>>>>> Stashed changes
class PersonalInfoViewModel(val db: FirebaseFirestore, val emailID: String): ViewModel() {
    private val _personalInfo = MutableLiveData<PersonalInfo>()
    val personalInfo : LiveData<PersonalInfo> get() = _personalInfo

    init {
        getPersonalInfoFromFirebase()
    }

    fun getPersonalInfoFromFirebase(){
        Log.i("PersonalInfo", "hello???")
        db.collection("user").whereEqualTo("userEmail", emailID).get().addOnSuccessListener { documents ->
            for (doc in documents) {
                _personalInfo.value  = doc.toObject()
                Log.i("HealthInfoHistory", _personalInfo.value.toString())
            }
        }.addOnFailureListener {
            Log.d("personalInfo", "Why failed?")
        }
    }
}