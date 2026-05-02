package com.raj.mygrowth.uiState

import com.raj.mygrowth.domain.WorkoutPlanItem


sealed class WorkoutListItem {
    data class Header(
        val day: String,
        val muscles: List<String>
    ) : WorkoutListItem()

    data class ExerciseItem(
        val exercise: List<WorkoutPlanItem>
    ) : WorkoutListItem()
}