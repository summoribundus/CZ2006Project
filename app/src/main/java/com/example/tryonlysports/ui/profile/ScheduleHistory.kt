package com.example.tryonlysports.ui.profile

import com.google.firebase.Timestamp

data class ScheduleHistory(val startDateTime: Timestamp?=null,
                           val endDateTime: Timestamp?=null,
                           val location: String?=null,
                           val userName: String?=null,
                           val description: String?=null,
                           val completed: Boolean?=null,
                           val outdoor: Boolean?=null){
    lateinit var id: String
}
