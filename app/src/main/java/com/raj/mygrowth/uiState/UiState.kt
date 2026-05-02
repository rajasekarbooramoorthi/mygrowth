package com.raj.mygrowth.uiState

import com.raj.mygrowth.domain.WorkoutResponse

sealed class UiState {
    object Loading : UiState()
    data class Success(val data: WorkoutResponse) : UiState()
    data class Error(val message: String) : UiState()
}