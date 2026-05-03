package com.raj.mygrowth.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.raj.mygrowth.domain.RequestAction
import com.raj.mygrowth.domain.ResponseAttendance
import com.raj.mygrowth.domain.SprintMasterResponse
import com.raj.mygrowth.domain.SprintTaskResponse
import com.raj.mygrowth.networkUtility.ApiService
import com.raj.mygrowth.networkUtility.RetrofitClient
import com.raj.mygrowth.repository.Repository
import com.raj.mygrowth.uiState.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CommonViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val api = RetrofitClient.instance.create(ApiService::class.java)

    fun fetchWorkoutPlan() {
        viewModelScope.launch {
            repository.getWorkoutPlan().onStart {
                _uiState.value = UiState.Loading
            }.catch { e ->
                _uiState.value = UiState.Error(e.message ?: "Something went wrong")
            }.collect { data ->
                _uiState.value = UiState.SuccessWorkout(data)
            }
        }
    }

    fun fetchDietPlan() {
        viewModelScope.launch {
            repository.getDietPlan().onStart {
                _uiState.value = UiState.Loading
            }.catch { e ->
                _uiState.value = UiState.Error(e.message ?: "Something went wrong")
            }.collect { data ->
                _uiState.value = UiState.SuccessDiet(data)
            }
        }
    }

    fun getAttendance(request: RequestAction): Flow<ResponseAttendance> = flow {
        emit(api.getAttendance(request))
    }.flowOn(Dispatchers.IO)

    fun getSprint(request: RequestAction): Flow<SprintMasterResponse> = flow {
        emit(api.getSprint(request))
    }.flowOn(Dispatchers.IO)

    fun getSprintTask(request: RequestAction): Flow<SprintTaskResponse> = flow {
        emit(api.getSprintTask(request))
    }.flowOn(Dispatchers.IO)

    fun fetchAttendance(request: RequestAction) {
        viewModelScope.launch {
            getAttendance(request).onStart {
                _uiState.value = UiState.Loading
            }.catch { e ->
                _uiState.value = UiState.Error(e.message ?: "Something went wrong")
            }.collect { data ->
                _uiState.value = UiState.SuccessTodoAttendance(data)
            }
        }
    }

    fun fetchSprint(request: RequestAction) {
        viewModelScope.launch {
            getSprint(request).onStart {
                _uiState.value = UiState.Loading
            }.catch { e ->
                _uiState.value = UiState.Error(e.message ?: "Something went wrong")
            }.collect { data ->
                _uiState.value = UiState.SuccessSprintMaster(data)
            }
        }
    }

    fun fetchSprintTask(request: RequestAction) {
        viewModelScope.launch {
            getSprintTask(request).onStart {
                _uiState.value = UiState.Loading
            }.catch { e ->
                _uiState.value = UiState.Error(e.message ?: "Something went wrong")
            }.collect { data ->
                _uiState.value = UiState.SuccessSprintTask(data)
            }
        }
    }

    class CommonViewModelFactory(
        private val repository: Repository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CommonViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST") return CommonViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}