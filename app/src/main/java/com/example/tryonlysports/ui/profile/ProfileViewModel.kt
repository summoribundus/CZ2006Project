package com.example.tryonlysports.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.util.*

/**
 * This is the ViewModel that stores and manages UI related data in the lifecycle of the profile fragment.
 *
 * @property db the firebase database.
 * @property name the user name to get information from firebase database.
 *
 * @author Li Rui, Liu Zhixuan
 */
class ProfileViewModel(val db: FirebaseFirestore, val name: String,val userId:String): ViewModel() {
    /**
     * The workoutHistory as mutable live private data.
     */
    private val _nearest_whistory = MutableLiveData<WorkoutHistory>()
    /**
     * The workoutHistory as live data.
     */
    val nearest_whistory : LiveData<WorkoutHistory> get() = _nearest_whistory

    /**
     * The scheduleHistory as mutable live private data.
     */
    private val _nearest_shistory = MutableLiveData<ScheduleHistory>()
    /**
     * The scheduleHistory as live data.
     */
    val nearest_shistory : LiveData<ScheduleHistory> get() = _nearest_shistory

    /**
     * The healthInfoHistory as mutable live private data.
     */
    private val _nearest_hhistory = MutableLiveData<HealthInfoHistory>()
    /**
     * The healthInfoHistory as live data.
     */
    val nearest_hhistory : LiveData<HealthInfoHistory> get() = _nearest_hhistory

    /**
     * Will be called when initialized.
     */
    init {
        getData()
    }

    /**
     * Calls the functions to get nearest histories of healthInfo, schedule and workout records.
     *
     */
    fun getData() {
        getNearestHealthHistoryFromFirebase()
        getNearestScheduleHistoryFromFirebase()
        getNearestWorkoutHistoryFromFirebase()
    }

    /**
     * Get the nearest workout record from firebase data store
     *
     */
    fun getNearestWorkoutHistoryFromFirebase(){
        db.collection("workoutHistory").whereEqualTo("userName", name).orderBy("date").limit(1).get().addOnSuccessListener { documents ->
            for (doc in documents) {
                val wh: WorkoutHistory = doc.toObject()
                wh.id = doc.id
                _nearest_whistory.value = wh
                Log.i("NearestWorkoutHistory", wh.toString())
            }
        }.addOnFailureListener {
            Log.d("nearest_workoutDAO", "Why failed?")
        }
    }
    /**
     * Get the nearest schedule record from firebase data store
     *
     */
    fun getNearestScheduleHistoryFromFirebase(){
        db.collection("schedule")
                .document(userId)
                .collection("activities")

                .get().addOnSuccessListener { documents ->
                    if(!documents.isEmpty){
                        val doc=documents.last()
                        val sh = toScheduleHistory(doc.data)
                        sh.id = doc.id
                        _nearest_shistory.value = sh
                    }


        }.addOnFailureListener {
            Log.d("nearest_ScheduleDAO", "Why failed?")
        }
    }
    /**
     * Get the nearest health info record from firebase data store
     *
     */
    fun getNearestHealthHistoryFromFirebase(){
        db.collection("healthInfo").whereEqualTo("userName", name).orderBy("recordTime").limit(1).get().addOnSuccessListener { documents ->
            for (doc in documents) {
                val hh: HealthInfoHistory = doc.toObject()
                hh.id = doc.id
                _nearest_hhistory.value = hh
                Log.i("NearestHealthHistory", hh.toString())
            }
        }.addOnFailureListener {
            Log.d("nearest_HealthDAO", "Why failed?")
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
        val cal= Calendar.getInstance()
        cal.set(nums[2], nums[1], nums[0], nums[3], nums[4])
        val dt=cal.time
        return com.google.firebase.Timestamp(dt)
    }

}
