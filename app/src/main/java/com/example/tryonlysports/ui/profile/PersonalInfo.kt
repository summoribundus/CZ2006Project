package com.example.tryonlysports.ui.profile

/**
 * This class is a data class for storing personal Info.
 *
 * @property phoneNumber phone number of the user.
 * @property region region that the user is located.
 * @property userEmail email account of the user.
 * @property username username of the user.
 *
 * @author Liu Zhixuan, Ye Ziyuan
 */
data class PersonalInfo(
val phoneNumber: String?=null,
val region: String?=null,
val userEmail: String?=null,
val username: String?=null) {
}
