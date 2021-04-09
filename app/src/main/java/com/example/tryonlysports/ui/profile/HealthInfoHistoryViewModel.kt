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
 * This is the ViewModel that stores and manages UI related data in the lifecycle of the Health Info History fragment.
 *
 * @property db the firebase database.
 * @property userName the userName to get information from firebase database.
 *
 * @author Liu Zhixuan, Li Rui, Ye Ziyuan
 */
>>>>>>> Stashed changes
class HealthInfoHistoryViewModel(val db: FirebaseFirestore, val userName: String):ViewModel() {

    private val _healthhistory = MutableLiveData<MutableList<HealthInfoHistory>>()
    val healthhistory : LiveData<MutableList<HealthInfoHistory>> get() = _healthhistory

    init {
        getAllHealthHistoryFromFirebase()
    }

<<<<<<< Updated upstream
=======
    /**
     * Gets all of the health info histories of the user from Firebase
     *
     */
>>>>>>> Stashed changes
    fun getAllHealthHistoryFromFirebase(){
        Log.i("HealthHistory", "hello???")
        var list: MutableList<HealthInfoHistory> = mutableListOf()
        db.collection("healthInfo").whereEqualTo("userName", userName).orderBy("recordTime", Query.Direction.DESCENDING).get().addOnSuccessListener { documents ->
            for (doc in documents) {
                val hh: HealthInfoHistory = doc.toObject<HealthInfoHistory>()
                hh.id = doc.id
//                _healthhistory.value?.add(hh)
                list.add(hh)
                Log.i("HealthInfoHistory", hh.toString())
            }
            _healthhistory.value = list
        }.addOnFailureListener {
            Log.d("healthinfoDAO", "Why failed?")
        }


    }
}