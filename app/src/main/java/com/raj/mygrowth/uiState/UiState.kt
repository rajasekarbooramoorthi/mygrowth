package com.raj.mygrowth.uiState

import com.raj.mygrowth.domain.DietResponse
import com.raj.mygrowth.domain.ResponseAttendance
import com.raj.mygrowth.domain.ResponseSimple
import com.raj.mygrowth.domain.SprintMasterResponse
import com.raj.mygrowth.domain.SprintTaskItem
import com.raj.mygrowth.domain.SprintTaskResponse
import com.raj.mygrowth.domain.WorkoutResponse

sealed class UiState {
    object Loading : UiState()
    data class SuccessWorkout(val data: WorkoutResponse) : UiState()
    data class SuccessTodoAttendance(val data: ResponseAttendance) : UiState()
    data class SuccessSprintMaster(val data: SprintMasterResponse) : UiState()
    data class SuccessSprintTask(val data: SprintTaskResponse) : UiState()
    data class SuccessSprintTaskAdd(val data: ResponseSimple) : UiState()
    data class SuccessDiet(val data: DietResponse) : UiState()
    data class Error(val message: String) : UiState()
}