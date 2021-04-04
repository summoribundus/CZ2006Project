package com.example.tryonlysports.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

class HealthInfoHistoryViewModel(val db: FirebaseFirestore, val userName: String):ViewModel() {

    private val _healthhistory = MutableLiveData<MutableList<HealthInfoHistory>>()
    val healthhistory : LiveData<MutableList<HealthInfoHistory>> get() = _healthhistory

    init {
        getAllHealthHistoryFromFirebase()
    }

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