package com.raj.mygrowth.uiState

import com.raj.mygrowth.domain.DietResponse
import com.raj.mygrowth.domain.WorkoutResponse

sealed class UiState {
    object Loading : UiState()
    data class SuccessWorkout(val data: WorkoutResponse) : UiState()
    data class SuccessDiet(val data: DietResponse) : UiState()
    data class Error(val message: String) : UiState()
}