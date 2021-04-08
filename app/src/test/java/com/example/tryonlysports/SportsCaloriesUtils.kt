package com.example.tryonlysports

import android.util.Log
import com.example.tryonlysports.ui.sportsResult.SportsResultsUtil
import java.util.concurrent.TimeUnit

class SportsCaloriesUtils {

//    val type = "cycling"
//    val speed = 9.0
//
//
//    fun calculateCalories() {
//        //METs x 3.5 x (your body weight in kilograms) / 200 =
//        val MET = SportsResultsUtil.checkMETs(type, speed) * TimeUnit.MILLISECONDS.toMinutes(
//                passedTime
//        )
//        Log.i("MET", MET.toString())
////        getWeight("Taylor123")
////        weighht = 50 //hardcoded 50 kg
//        //Log.i("Weight", getWeight("Taylor123").toString())
//        cal = MET * 3.5 * weight.value!!.toDouble() / 200.toDouble()
//
//        Log.i("Weight", weight.toString())
//        if (speed == 0.0 || totalDistance == 0.0)
//            cal = 0.0
//        _calories.value = String.format("Calories: %.2f Kcal", cal)
//    }
//
//
//    /**
//     * Gets weight from firebase for calories calculation.
//     *
//     * @param User the username to look for in database when fetching information.
//     */
//    private fun getWeight(name: String){
////        var  weight_retrieved: Long = 0
//        db.collection("healthInfo").whereEqualTo("userName", name).
//        orderBy("recordTime").limit(1).get().addOnSuccessListener { documents ->
//            for (doc in documents) {
//                _weight.value = doc.data["value"] as Long
////                weight_retrieved = doc.doata["value"] as Long
//                Log.i("WeightFromKotlin", _weight.value.toString())
//
//            }
//        }.addOnFailureListener {
//            Log.d("DAO", "Why failed?")
//        }
//
//    }
}