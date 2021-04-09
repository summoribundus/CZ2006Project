package com.example.tryonlysports.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

/**
 * This is the ViewModel that stores and manages UI related data in the lifecycle of the schedule history fragment.
 *
 * @property db the firebase database.
 * @property userName the user name to get information from firebase database.
 *
 * @author Liu Zhixuan, Li Rui
 */
class ScheduleHistoryViewModel(val db: FirebaseFirestore, val userName: String): ViewModel() {
    /**
     * The scheduleHistory as mutable live private data.
     */
    private val _shistory = MutableLiveData<MutableList<ScheduleHistory>>()

    /**
     * The scheduleHistory as live data.
     */
    val shistory : LiveData<MutableList<ScheduleHistory>> get() = _shistory

    /**
     * Will be called when initialized.
     */
    init {
        getAllScheduleHistoryFromFirebase()
    }

    /**
     * Get all of the schedule histories of the user from firebase data store.
     *
     */
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
