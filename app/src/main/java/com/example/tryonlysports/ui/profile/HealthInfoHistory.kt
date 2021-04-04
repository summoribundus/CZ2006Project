package com.example.tryonlysports.ui.profile

import com.google.firebase.Timestamp

data class HealthInfoHistory(val recordTime: Timestamp?= null,
                             val value: Int?=null,
                             val valueType: String?=null,
                             val userName: String?=null
                            ){
    lateinit var id:String
}
