package com.example.tryonlysports.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tryonlysports.ui.schedule.Activity
import com.example.tryonlysports.ui.schedule.DateTime
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

/**
 * This is the ViewModel that stores and manages UI related data in the lifecycle of the schedule history fragment.
 *
 * @property db the firebase database.
 * @property userName the user name to get information from firebase database.
 *
 * @author Ye Ziyuan, Wang Qiaochu, Liu Zhixuan
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
        db.collection("schedule")
                .document(userName)
                .collection("activities")

                .whereEqualTo("completed",true)
                .get()
                .addOnSuccessListener { documents ->
            for (doc in documents) {
                val sh = toScheduleHistory(doc.data)
                sh.id = doc.id
                list.add(sh)
                Log.i("ScheduleHistory", sh.toString())
            }
            _shistory.value = list
        }.addOnFailureListener {
            Log.d("scheduleDAO", "Why failed?")
        }
    }

    private fun toScheduleHistory(map:Map<String, Any>):ScheduleHistory{
        val location=map.get("location") as String
        val description=map.get("description") as String
        val outdoor=map.get("isOutdoor") as Boolean
        val completed=map.get("completed") as Boolean
        val start=stringToDateTime(map.get("startTime") as String)
        val end=stringToDateTime(map.get("endTime") as String)
        return ScheduleHistory(start,end,location,"",description,completed,outdoor)
    }

    private fun stringToDateTime(str:String):com.google.firebase.Timestamp {
        val nums = str.split("/").map { it.toInt() }.toTypedArray()
        val cal=Calendar.getInstance()
        cal.set(nums[2], nums[1], nums[0], nums[3], nums[4])
        val dt=cal.time
        return com.google.firebase.Timestamp(dt)
    }
}
