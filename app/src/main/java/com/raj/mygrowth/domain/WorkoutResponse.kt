package com.raj.mygrowth.domain

data class WorkoutResponse(val workoutPlan: List<WorkoutPlan>)


data class WorkoutPlan(
    val day: String,
    val musclesWorked: List<String>,
    val exercises: List<WorkoutPlanItem>,
)

data class WorkoutPlanItem(
    val id: String,
    val name: String,
    val sets: String,
    val reps: String,
    val rest: String,
    val focus: String,
    val notes: String,
)
