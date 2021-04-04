package com.example.tryonlysports.ui.profile

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("dateWorkoutHistoryText")
fun TextView.setDateWorkoutHistoryText(item: WorkoutHistory) {
    text = item.date?.toDate().toString()
}

@BindingAdapter("typeWorkoutHistoryText")
fun TextView.setTypeWorkoutHistoryText(item: WorkoutHistory) {
    text = item.workoutType.toString()
}

@BindingAdapter("speedWorkoutHistoryText")
fun TextView.setSpeedWorkoutHistoryText(item: WorkoutHistory) {
    text = item.avgSpeed.toString()
}

@BindingAdapter("durationWorkoutHistoryText")
fun TextView.setDurationWorkoutHistoryText(item: WorkoutHistory) {
    text = item.duration.toString()
}

@BindingAdapter("caloriesWorkoutHistoryText")
fun TextView.setCaloriesWorkHistoryText(item: WorkoutHistory) {
    text = item.calories.toString()
}

@BindingAdapter("distanceWorkoutHistoryText")
fun TextView.setDistanceWorkoutHistoryText(item: WorkoutHistory) {
    text = item.distance.toString()
}

@BindingAdapter("dateWeightHistoryListText")
fun TextView.setDateWeightHistoryListText(item: HealthInfoHistory) {
    text = item.recordTime?.toDate().toString()
}

@BindingAdapter("weightHistoryListText")
fun TextView.setWeightHistoryListText(item: HealthInfoHistory) {
    text = item.value.toString()
}

@BindingAdapter("description")
fun TextView.setDescription(item: ScheduleHistory) {
    text = item.description
}

@BindingAdapter("location")
fun TextView.setLocation(item: ScheduleHistory) {
    text = item.location
}

@BindingAdapter("starttime")
fun TextView.setStartTime(item: ScheduleHistory) {
    text = item.startDateTime?.toDate().toString()
}

@BindingAdapter("endtime")
fun TextView.setEndTime(item: ScheduleHistory) {
    text = item.endDateTime?.toDate().toString()
}