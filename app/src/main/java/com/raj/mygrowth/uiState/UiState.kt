package com.raj.mygrowth.uiState

import com.raj.mygrowth.domain.DietResponse
import com.raj.mygrowth.domain.ResponseAttendance
import com.raj.mygrowth.domain.ResponseGetAttendance
import com.raj.mygrowth.domain.ResponseQuitZillaBenefits
import com.raj.mygrowth.domain.ResponseQuitZillaMaster
import com.raj.mygrowth.domain.ResponseQuitZillaMotivation
import com.raj.mygrowth.domain.ResponseQuitZillaQuote
import com.raj.mygrowth.domain.ResponseSimple
import com.raj.mygrowth.domain.SprintMasterResponse
import com.raj.mygrowth.domain.SprintTaskResponse
import com.raj.mygrowth.domain.ThirukuralResponse
import com.raj.mygrowth.domain.ThirukuralResponseLatest
import com.raj.mygrowth.domain.WeightGainResponse
import com.raj.mygrowth.domain.WorkoutResponse

sealed class UiState {
    object Loading : UiState()
    data class SuccessWorkout(val data: WorkoutResponse) : UiState()
    data class SuccessTodoAttendance(val data: ResponseAttendance) : UiState()
    data class SuccessGetAddAttendance(val data: ResponseGetAttendance) : UiState()
    data class SuccessSprintMaster(val data: SprintMasterResponse) : UiState()
    data class SuccessSprintTask(val data: SprintTaskResponse) : UiState()
    data class SuccessSprintTaskAdd(val data: ResponseSimple) : UiState()
    data class SuccessWeightGainTimeLine(val data: WeightGainResponse) : UiState()
    data class SuccessQuitZillaReport(val data: ResponseQuitZillaMaster) : UiState()
    data class SuccessQuitZillaMotivate(val data: ResponseQuitZillaMotivation) : UiState()
    data class SuccessTrack(val data: ResponseQuitZillaMaster) : UiState()
    data class SuccessQuitZillaBenefits(val data: ResponseQuitZillaBenefits) : UiState()
    data class SuccessQuotes(val data: ResponseQuitZillaQuote) : UiState()
    data class SuccessDiet(val data: DietResponse) : UiState()
    data class SuccessThirukural(val data: ThirukuralResponseLatest) : UiState()
    data class Error(val message: String) : UiState()
}