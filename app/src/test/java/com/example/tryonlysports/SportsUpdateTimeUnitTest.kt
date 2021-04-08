package com.example.tryonlysports

import android.widget.TextView
import org.junit.Assert.*
import org.junit.Test

class SportsUpdateTimeUnitTest {
    @Test
    fun UpdateTimeUnitTest(){
        val util = SportsUpdatesUtil()
        val currenttime = System.currentTimeMillis()

        assertEquals((System.currentTimeMillis() - currenttime).toDouble(), util.passedTimeValue.toDouble(), 0.1)

        val util1 = SportsUpdatesUtil()
        val currenttime1 = System.currentTimeMillis()

        assertNotEquals((System.currentTimeMillis() - currenttime1 + 100).toDouble(), util1.passedTimeValue.toDouble(), 0.1)

    }
}