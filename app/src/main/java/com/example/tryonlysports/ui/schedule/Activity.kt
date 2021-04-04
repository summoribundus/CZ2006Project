package com.example.tryonlysports.ui.schedule

data class Activity (
        var description:String="",
        var location:String="",
        var start:DateTime,
        var end:DateTime,
        var isOutdoor:Boolean,
        var complete:Boolean=false,
        var id:String=""){

}