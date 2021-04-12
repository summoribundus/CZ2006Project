package com.example.tryonlysports.ui.profile

import com.google.firebase.Timestamp

/**
 * This class is a data class for storing the schedule histories.
 *
 * @property startDateTime date time of the starting time of the scheduled activity.
 * @property endDateTime date time of the ending time of the scheduled activity.
 * @property location location of the scheduled activity.
 * @property userName userName of the user that the record belongs to.
 * @property description name of the scheduled activity.
 * @property completed indicates whether the activity is completed or not.
 * @property outdoor indicates whether the activity is an outdoor activity.
 *
 * @author Ye Ziyuan, Wang Qiaochu, Liu Zhixuan
 */
data class ScheduleHistory(val startDateTime: Timestamp?=null,
                           val endDateTime: Timestamp?=null,
                           val location: String?=null,
                           val userName: String?=null,
                           val description: String?=null,
                           val completed: Boolean?=null,
                           val outdoor: Boolean?=null){
    /**
     * The user email for searching in firebase.
     */
    lateinit var id: String
}
