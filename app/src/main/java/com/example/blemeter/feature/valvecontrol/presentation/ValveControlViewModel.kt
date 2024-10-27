package com.example.blemeter.feature.valvecontrol.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.ble.domain.model.request.ValveInteractionCommand
import com.example.blemeter.feature.dashboard.domain.usecases.DashboardUseCases
import com.example.blemeter.config.model.ValveControlData
import com.example.blemeter.config.model.ValveStatus
import com.example.blemeter.feature.dashboard.domain.usecases.ObserveDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ValveControlViewModel @Inject constructor(
    private val useCases: DashboardUseCases,
    private val observeDataUseCase: ObserveDataUseCase,
) : ViewModel() {

    companion object {
        const val TAG = "ValveControlViewModel"
    }

    init {
        observeResponse()
    }

    private val _uiState: MutableStateFlow<ValveControlUiState> by lazy {
        MutableStateFlow(ValveControlUiState())
    }
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: ValveControlUiEvent) {
        when (event) {
            is ValveControlUiEvent.OnValveControl -> onValveControl(event.command)
        }
    }

    private fun onValveControl(status: ValveInteractionCommand) {
        viewModelScope.launch {
            useCases.valveControlUseCase(status = status)
                .onFailure {  }
                .onSuccess {  }
        }
    }

    private fun observeResponse() {
        viewModelScope.launch {
            observeDataUseCase(
                service = MeterServicesProvider.MainService.SERVICE,
                observeCharacteristic = MeterServicesProvider.MainService.NOTIFY_CHARACTERISTIC
            )?.catch { cause ->

            }?.collect { data ->
                Log.e(TAG, "observerResponse :: $data")
                when (data) {
                    is ValveControlData -> {
                        if (data.controlState is ValveStatus) {
                            _uiState.update { it.copy(valveStatus = data.controlState) }
                        }
                    }
                }
            }
        }
    }
}