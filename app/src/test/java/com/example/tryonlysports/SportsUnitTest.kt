package com.example.tryonlysports

import android.widget.TextView
import com.example.tryonlysports.ui.sportsResult.SportsResultsUtil
import org.junit.Assert.*
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SportsUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testCheckMET(){
        val mettest1 = SportsResultsUtil.checkMETs("jogging", 8.0)
        assertEquals(mettest1, 12.5, 0.00001)

        val mettest2 = SportsResultsUtil.checkMETs("walking", 3.0)
        assertEquals(mettest2, 3.5, 0.00001)

        val mettest3 = SportsResultsUtil.checkMETs("cycling", 20.0)
        assertEquals(mettest3, 15.8, 0.00001)

        val mettest4 = SportsResultsUtil.checkMETs("cycling", -100.0)
        assertEquals(mettest4, -1.0, 0.00001)
    }


}