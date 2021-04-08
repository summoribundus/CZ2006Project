package com.example.tryonlysports

import android.util.Log
import com.example.tryonlysports.ui.sportsResult.SportsResultsUtil
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class SportsCaloriesUtils(val username: String) {

    val type = "cycling"
    val speed = 9.0
    val passedTime = 36000000.toLong()
    var cal by Delegates.notNull<Double>()
    var weight = 0
    var totalDistance = 1000.0
    var calories  = ""
    var MET = 0.0


    fun calculateCalories() {
        //METs x 3.5 x (your body weight in kilograms) / 200 =
        MET = SportsResultsUtil.checkMETs(type, speed) * TimeUnit.MILLISECONDS.toMinutes(
                passedTime
        )

        getWeight(username)

        cal = MET * 3.5 * weight.toDouble() / 200.toDouble()

        if (speed == 0.0 || totalDistance == 0.0)
            cal = 0.0
        calories = String.format("Calories: %.2f Kcal", cal)
    }

    private fun getWeight(name: String){
        if (name in listOf<String>("todaytest@gmail.com", "test@gmail.com") )
            weight = 50
        else {
            weight  = 0
        }
    }
}