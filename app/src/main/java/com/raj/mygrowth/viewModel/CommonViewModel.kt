package com.raj.mygrowth.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.raj.mygrowth.networkUtility.ApiService
import com.raj.mygrowth.networkUtility.RetrofitClient
import com.raj.mygrowth.repository.Repository
import com.raj.mygrowth.uiState.UiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CommonViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    val api = RetrofitClient.instance.create(ApiService::class.java)

    init {
        fetchWorkoutPlan()
    }

    fun fetchWorkoutPlan() {
        viewModelScope.launch {
            repository.getWorkoutPlan()
                .onStart {
                    _uiState.value = UiState.Loading
                }
                .catch { e ->
                    _uiState.value = UiState.Error(e.message ?: "Something went wrong")
                }
                .collect { data ->
                    _uiState.value = UiState.Success(data)
                }
        }
    }

    class CommonViewModelFactory(
        private val repository: Repository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CommonViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CommonViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}