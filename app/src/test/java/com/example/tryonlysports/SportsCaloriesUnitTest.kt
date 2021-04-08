package com.example.tryonlysports

import android.util.Log
import android.widget.TextView
import com.example.tryonlysports.ui.sportsResult.SportsResultsUtil
import org.junit.Assert.*
import org.junit.Test

class SportsCaloriesUnitTest {
    @Test
    fun CaloriesCalculationUnitTest(){
        val mockCalories1 = SportsCaloriesUtils("todaytest@gmail.com")
        mockCalories1.calculateCalories()
        assertEquals(mockCalories1.weight.toDouble(), 50.0, 0.0)
        assertEquals(mockCalories1.username, "todaytest@gmail.com")
        assertEquals(mockCalories1.MET, 2400.0, 0.0)
        assertEquals(mockCalories1.cal, 2100.0, 0.0)
        assertEquals(mockCalories1.calories, "Calories: 2100.00 Kcal")

        val mockCalories2 = SportsCaloriesUtils("samuel@qq.com")
        mockCalories2.calculateCalories()
        assertEquals(mockCalories2.weight.toDouble(), 0.0, 0.0)
        assertEquals(mockCalories2.username, "samuel@qq.com")
        assertEquals(mockCalories2.MET, 2400.0, 0.0)
        assertEquals(mockCalories2.cal, 0.0, 0.0)
        assertEquals(mockCalories2.calories, "Calories: 0.00 Kcal")
    }
}