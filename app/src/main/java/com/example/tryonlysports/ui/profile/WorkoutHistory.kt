package com.example.tryonlysports.ui.profile

import com.google.firebase.Timestamp

/**
 * This class is a data class for storing the workout histories.
 *
 * @property date date of the workout record.
 * @property avgSpeed average speed of the workout record.
 * @property duration duration of the workout record.
 * @property userName userName of the user that the workout record belongs to.
 * @property distance distance of the workout record.
 * @property calories calories burned of the workout record.
 * @property workoutType workout type (e.g. cycling) of the workout record.
 * @author  Ye Ziyuan, Liu Zhixuan
 */
data class WorkoutHistory(val date: Timestamp?=null,
                          val avgSpeed: String?=null,
                          val duration: String?=null,
                          val userName: String?=null,
                          val distance: String?=null,
                          val calories: String?=null,
                        val workoutType: String?=null){
    /**
     * The user email for searching in firebase.
     */
    lateinit var id: String
}
