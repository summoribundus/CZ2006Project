package com.example.tryonlysports.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

class ScheduleHistoryViewModel(val db: FirebaseFirestore, val userName: String): ViewModel() {

    private val _shistory = MutableLiveData<MutableList<ScheduleHistory>>()
    val shistory : LiveData<MutableList<ScheduleHistory>> get() = _shistory

    init {
        getAllScheduleHistoryFromFirebase()
    }

    fun getAllScheduleHistoryFromFirebase(){
        var list: MutableList<ScheduleHistory> = mutableListOf()
        db.collection("schedule").whereEqualTo("userName", userName).orderBy("startDateTime", Query.Direction.DESCENDING).get().addOnSuccessListener { documents ->
            for (doc in documents) {
                val sh: ScheduleHistory = doc.toObject<ScheduleHistory>()
                sh.id = doc.id
                list.add(sh)
                Log.i("ScheduleHistory", sh.toString())
            }
            _shistory.value = list
        }.addOnFailureListener {
            Log.d("scheduleDAO", "Why failed?")
        }
    }

}