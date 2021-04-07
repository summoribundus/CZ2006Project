package com.example.tryonlysports.ui.schedule

/**
 * This class is a data class for storing activities
 *
 * @property description name of the activity
 * @property location location of activity
 * @property start date time of starting time of activity
 * @property end date time of ending time of activity
 * @property complete indicate whether the activity is completed or not
 * @property isOutdoor indicating wheher the activity is an outdoor activity
 * @property id auto-generated id for the activity
 */
data class Activity (
        var description:String="",
        var location:String="",
        var start:DateTime,
        var end:DateTime,
        var isOutdoor:Boolean,
        var complete:Boolean=false,
        var id:String=""){

}