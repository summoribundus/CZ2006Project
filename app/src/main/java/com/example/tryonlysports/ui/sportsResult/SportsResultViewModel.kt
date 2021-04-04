package com.example.tryonlysports.ui.sportsResult

import com.google.firebase.FirebaseApp
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.type.Date
import java.util.*
import java.util.concurrent.TimeUnit


class SportsResultViewModel(
    val type: String,
    val passedTime: Long,
    val totalDistance: Double,
    val db: FirebaseFirestore,
    val username: String
): ViewModel() {

    private val _avgSpeed = MutableLiveData<String>()
    val avgspeed : LiveData<String> get() = _avgSpeed

    private val _passed_Time = MutableLiveData<String>()
    val passed_Time: LiveData<String> get() = _passed_Time

    private val _calories = MutableLiveData<String>()
    val calories : LiveData<String> get() = _calories

    private val _total_Distance = MutableLiveData<String>()
    val total_Distance : LiveData<String> get() = _total_Distance

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> get() = _loading

    private val _weight = MutableLiveData<Long>()
    val weight : LiveData<Long> get() = _weight



    init {
        initProperty()
    }

    private var speed = 0.0
    private var cal = 0.0
    //private val _weight = MutableLiveData<Long>()
    //val weight : LiveData<Long> get() = _weight

    private fun initProperty() {

        speed = totalDistance * 1000 / TimeUnit.MILLISECONDS.toSeconds(passedTime)*2.23694
        _avgSpeed.value = String.format("Average speed: %.0f mph", speed)
        _loading.value = true
        getWeight(username)
        _passed_Time.value = String.format(
            "Duration: %d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(
                passedTime
            ),
            TimeUnit.MILLISECONDS.toSeconds(passedTime) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(passedTime))
        )
        formatRouteLength(totalDistance)

    }

    fun setLoadingFalse() {
        _loading.value = false
    }


    fun calculateCalories() {
        //METs x 3.5 x (your body weight in kilograms) / 200 =
        val MET = SportsResultsUtil.checkMETs(type, speed) * TimeUnit.MILLISECONDS.toMinutes(
            passedTime
        )
        Log.i("MET", MET.toString())
//        getWeight("Taylor123")
//        weighht = 50 //hardcoded 50 kg
        //Log.i("Weight", getWeight("Taylor123").toString())
        cal = MET * 3.5 * weight.value!!.toDouble() / 200.toDouble()

        Log.i("Weight", weight.toString())
        if (speed == 0.0 || totalDistance == 0.0)
            cal = 0.0
        _calories.value = String.format("Calories: %.2f Kcal", cal)
    }



    private fun getWeight(name: String){
//        var  weight_retrieved: Long = 0
        db.collection("healthInfo").whereEqualTo("userName", name).
        orderBy("recordTime").limit(1).get().addOnSuccessListener { documents ->
            for (doc in documents) {
                _weight.value = doc.data["value"] as Long
//                weight_retrieved = doc.doata["value"] as Long
                Log.i("WeightFromKotlin", _weight.value.toString())

            }
        }.addOnFailureListener {
            Log.d("DAO", "Why failed?")
        }

//        return weight_retrieved
    }

    fun formatRouteLength(totalDistance: Double){
        _total_Distance.value = String.format("Total Distance: %.3f km", totalDistance)
        //divided by 1000 to get the distance in km
    }

    fun saveToFirebase(){
        val workoutHistory: CollectionReference = db.collection("workoutHistory")


        val formattedDate: Timestamp = Timestamp.now()

        val history = WorkoutHistory(formattedDate, _avgSpeed.value!!, _passed_Time.value!!, username, _total_Distance.value!!, _calories.value!!, type)

            // Add a new document to the restaurants collection
        workoutHistory.add(history)

    }

    inner class WorkoutHistory(
        val date: Timestamp,
        val avgSpeed: String,
        val duration: String,
        val userName: String,
        val distance: String,
        val calories: String,
        val workoutType: String,
    )

}
