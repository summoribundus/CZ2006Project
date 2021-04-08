package com.example.tryonlysports.ui.sportsResult

/**
 * This is a util class that checks the type of workout and the avg speed, and returns the MET.
 *
 * @author Ye Ziyuan
 */
internal object SportsResultsUtil{

    /**
     * Calculates MET per minutes based on type of workout and average speed.
     *
     * @param type the workout type (e.g. jogging/walking/cycling).
     * @param avgSpeed the average speed of the user workout.
     * @return MET value per minute.
     */
    fun checkMETs(type: String, avgSpeed: Double): Double{
        if (type != "jogging"  && type != "walking" && type!="cycling")
            return -1.0
        if (avgSpeed>=0){
            val speed = kotlin.math.floor(avgSpeed)
            if (type == "jogging"){
                if (avgSpeed <= 6)
                    return 9.0
                else if (avgSpeed <= 7){
                    return 11.0
                }
                else if (avgSpeed <= 8){
                    return 12.5
                }
                else if (avgSpeed <= 9){
                    return 14.0
                }
                else if (avgSpeed <= 9){
                    return 16.0
                }
                else {
                    return 18.0
                }
            }
            else if (type == "walking"){
                if (avgSpeed <= 2)
                    return 2.5
                else if (avgSpeed <= 2.5){
                    return 3.0
                }
                else if (avgSpeed <= 3){
                    return 3.5
                }
                else if (avgSpeed <= 4){
                    return 4.0
                }
                else if (avgSpeed <= 4.5){
                    return 4.5
                }
                else {
                    return 6.5
                }
            }
            else {
                if (avgSpeed <= 10)
                    return 4.0
                else if (avgSpeed <= 11.9){
                    return 6.8
                }
                else if (avgSpeed <= 13.9){
                    return 8.0
                }
                else if (avgSpeed <= 15.9){
                    return 10.0
                }
                else if (avgSpeed <= 19){
                    return 12.0
                }
                else {
                    return 15.8
                }
            }
        } else return -1.0

    }
}