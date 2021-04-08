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
        //valid inputs
        val mettest1 = SportsResultsUtil.checkMETs("jogging", 8.0)
        assertEquals(mettest1, 12.5, 0.00001)

        val mettest2 = SportsResultsUtil.checkMETs("walking", 8.0)
        assertEquals(mettest2, 6.5, 0.00001)

        val mettest3 = SportsResultsUtil.checkMETs("cycling", 8.0)
        assertEquals(mettest3, 4.0, 0.00001)

        //invalid average speed
        val mettest4 = SportsResultsUtil.checkMETs("cycling", -100.0)
        assertEquals(mettest4, -1.0, 0.00001)

        val mettest5 = SportsResultsUtil.checkMETs("jogging", -100.0)
        assertEquals(mettest5, -1.0, 0.00001)

        val mettest6 = SportsResultsUtil.checkMETs("walking", -100.0)
        assertEquals(mettest6, -1.0, 0.00001)

        //invalid workout type
        val mettest7 = SportsResultsUtil.checkMETs("student", 10.0)
        assertEquals(mettest7, -1.0, 0.00001)

        //invalid workout type and invalid avg speed
        val mettest8 = SportsResultsUtil.checkMETs("abc", -50.0)
        assertEquals(mettest8, -1.0, 0.00001)

    }


}