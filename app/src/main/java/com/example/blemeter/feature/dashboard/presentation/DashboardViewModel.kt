package com.example.blemeter.feature.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blemeter.feature.dashboard.domain.model.MeterControl
import com.example.blemeter.feature.dashboard.domain.usecases.DashboardUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val useCases: DashboardUseCases
) : ViewModel() {

    companion object {
        const val TAG = "DashboardViewModel"
    }

    init {
        readMeterData()
    }

    private val _uiState : MutableStateFlow<DashboardUiState> by lazy {
        MutableStateFlow(DashboardUiState())
    }
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: DashboardUiEvent) {
        when(event) {
            is DashboardUiEvent.OnMeterControl -> onMeterControl(event.control)
        }
    }

    private fun onMeterControl(control: MeterControl) {
        when(control) {
            MeterControl.READ_DATA -> readMeterData()
            MeterControl.VALVE_CONTROL -> onValveControl()
            MeterControl.RECHARGE -> rechargeMeter()
            MeterControl.RESET_DATA -> resetMeterData()
            MeterControl.ACCUMULATE -> accumulateData()
        }
    }

    private fun accumulateData() {

    }

    private fun resetMeterData() {
        viewModelScope.launch {
            useCases.readMeterDataUseCase()
                .onFailure {  }
                .onSuccess {  }
        }
    }

    private fun rechargeMeter() {

    }

    private fun onValveControl() {

    }

    private fun readMeterData() {
        viewModelScope.launch {
            useCases.readMeterDataUseCase()
                .onFailure {  }
                .onSuccess {  }
        }
    }
}