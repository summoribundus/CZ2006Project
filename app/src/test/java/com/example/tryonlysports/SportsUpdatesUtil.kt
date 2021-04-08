package com.example.tryonlysports

import java.util.concurrent.TimeUnit

class SportsUpdatesUtil {

    var passedTimeValue = 0
    var timePassed = ""

    fun updateTimePassed(starttime: Long) {
        val tPassed: Long = System.currentTimeMillis() - starttime
        passedTimeValue = tPassed.toInt()
        timePassed = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(tPassed),
                TimeUnit.MILLISECONDS.toSeconds(tPassed) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(tPassed)))
    }
}