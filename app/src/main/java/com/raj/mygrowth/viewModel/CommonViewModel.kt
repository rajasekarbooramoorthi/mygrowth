package com.raj.mygrowth.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.raj.mygrowth.domain.RequestAction
import com.raj.mygrowth.domain.RequestActionAddAttendance
import com.raj.mygrowth.domain.RequestActionAddSprintTask
import com.raj.mygrowth.domain.ResponseAttendance
import com.raj.mygrowth.domain.ResponseGetAttendance
import com.raj.mygrowth.domain.ResponseQuitZillaMaster
import com.raj.mygrowth.domain.ResponseSimple
import com.raj.mygrowth.domain.SprintMasterResponse
import com.raj.mygrowth.domain.SprintTaskResponse
import com.raj.mygrowth.domain.WeightGainResponse
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

    fun getAddAttendance(request: RequestAction): Flow<ResponseGetAttendance> = flow {
        emit(api.getaddAttendance(request))
    }.flowOn(Dispatchers.IO)

    fun getSprint(request: RequestAction): Flow<SprintMasterResponse> = flow {
        emit(api.getSprint(request))
    }.flowOn(Dispatchers.IO)

  fun getQuitZillaReport(request: RequestAction): Flow<ResponseQuitZillaMaster> = flow {
        emit(api.getQuitZillaReport(request))
    }.flowOn(Dispatchers.IO)

    fun getSprintTask(request: RequestAction): Flow<SprintTaskResponse> = flow {
        emit(api.getSprintTask(request))
    }.flowOn(Dispatchers.IO)

    fun addSprintTaskRequest(request: RequestActionAddSprintTask): Flow<ResponseSimple> = flow {
        emit(api.addSprintTask(request))
    }.flowOn(Dispatchers.IO)

    fun addAttendanceRequest(request: RequestActionAddAttendance): Flow<ResponseSimple> = flow {
        emit(api.addAttendanceRequest(request))
    }.flowOn(Dispatchers.IO)

    fun getWeightGainTimeLine(): Flow<WeightGainResponse> = flow {
        emit(api.getWeightGainTimeLine())
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

    fun fetchGetAttendance(request: RequestAction) {
        viewModelScope.launch {
            getAddAttendance(request).onStart {
                _uiState.value = UiState.Loading
            }.catch { e ->
                _uiState.value = UiState.Error(e.message ?: "Something went wrong")
            }.collect { data ->
                _uiState.value = UiState.SuccessGetAddAttendance(data)
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

    fun fetchQuitZillaReport(request: RequestAction) {
        viewModelScope.launch {
            getQuitZillaReport(request).onStart {
                _uiState.value = UiState.Loading
            }.catch { e ->
                _uiState.value = UiState.Error(e.message ?: "Something went wrong")
            }.collect { data ->
                _uiState.value = UiState.SuccessQuitZillaReport(data)
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

    fun addSprintTask(request: RequestActionAddSprintTask) {
        viewModelScope.launch {
            addSprintTaskRequest(request).onStart {
                _uiState.value = UiState.Loading
            }.catch { e ->
                _uiState.value = UiState.Error(e.message ?: "Something went wrong")
            }.collect { data ->
                _uiState.value = UiState.SuccessSprintTaskAdd(data)
            }
        }
    }

    fun addAttendance(request: RequestActionAddAttendance) {
        viewModelScope.launch {
            addAttendanceRequest(request).onStart {
                _uiState.value = UiState.Loading
            }.catch { e ->
                _uiState.value = UiState.Error(e.message ?: "Something went wrong")
            }.collect { data ->
                _uiState.value = UiState.SuccessSprintTaskAdd(data)
            }
        }
    }

    fun fetchWeightGainTimeLine() {
        viewModelScope.launch {
            getWeightGainTimeLine().onStart {
                _uiState.value = UiState.Loading
            }.catch { e ->
                _uiState.value = UiState.Error(e.message ?: "Something went wrong")
            }.collect { data ->
                _uiState.value = UiState.SuccessWeightGainTimeLine(data)
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