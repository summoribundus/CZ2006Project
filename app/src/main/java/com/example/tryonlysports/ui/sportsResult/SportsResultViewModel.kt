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

/**
 * This is the ViewModel that stores and manages UI related data in the lifecycle of the Sports Result fragment.
 *
 * @property type the workout type. (e.g. walking/jogging/cycling)
 * @property passedTime the time duration of workout.
 * @property totalDistance the total distance of the user workout route.
 * @property db the firebase database.
 * @property username the user name to get information from firebase database.
 */
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

    /**
     * Manages the initial properties, Formats the data to display once the data is ready from firebase.
     *
     */
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

    /**
     * Sets loading status to be false.
     *
     */
    fun setLoadingFalse() {
        _loading.value = false
    }

    /**
     * Calculates the calories burned from this workout.
     *
     */
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


    /**
     * Gets weight from firebase for calories calculation.
     *
     * @param User the username to look for in database when fetching information.
     */
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

    /**
     * Formats the total length to display as a string.
     *
     * @param totalDistance the numerical value of total distance of the workout.
     */
    fun formatRouteLength(totalDistance: Double){
        _total_Distance.value = String.format("Total Distance: %.3f km", totalDistance)
        //divided by 1000 to get the distance in km
    }

    /**
     * Saves the workout history to firebase data store.
     *
     */
    fun saveToFirebase(){
        val workoutHistory: CollectionReference = db.collection("workoutHistory")


        val formattedDate: Timestamp = Timestamp.now()

        val history = WorkoutHistory(formattedDate, _avgSpeed.value!!, _passed_Time.value!!, username, _total_Distance.value!!, _calories.value!!, type)

            // Add a new document to the restaurants collection
        workoutHistory.add(history)

    }

    /**
     * The data model class to save to firebase as a WorkoutHistory object.
     *
     * @property date the date of the workout.
     * @property avgSpeed the avg speed of the workout.
     * @property duration the time duration of the workout.
     * @property userName the information of user name.
     * @property distance the total distance of the workout.
     * @property calories the total calories burned in this workout.
     * @property workoutType the workout type of this workout. (e.g. jogging/walking/cycling)
     */
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
