package com.example.blemeter.feature.connection.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blemeter.core.ble.data.repository.BLEService
import com.example.blemeter.core.ble.data.repository.IBLERepository
import com.example.blemeter.core.ble.domain.model.DeviceDetail
import com.example.blemeter.core.ble.domain.model.ScannedDevice
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.ble.domain.model.hasConfigCharacteristic
import com.example.blemeter.feature.connection.domain.usecases.ConnectionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnViewModel @Inject constructor(
    private val bleService: BLEService
) : ViewModel() {

    private var _scanningJob: Job? = null

    private val _uiState = MutableStateFlow(ConnectionUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeNearByDevices()
    }

    fun onEvent(event: ConnectionUiEvent) {
        when (event) {
            is ConnectionUiEvent.OnConnectionEstablish -> {

            }

            is ConnectionUiEvent.OnStartScan -> startScanning()
            is ConnectionUiEvent.OnStopScan -> stopScanning()
            is ConnectionUiEvent.BluetoothEvent -> { }
        }
    }

    private fun startScanning() {
        _scanningJob = viewModelScope.launch {
            bleService.observeAdvertisements()
            _uiState.update {
                it.copy(
                    isScanning = true
                )
            }
        }
    }

    private fun stopScanning() {
        _scanningJob?.cancel()
        _uiState.update {
            it.copy(
                isScanning = false
            )
        }
    }

    private fun observeNearByDevices() {
        viewModelScope.launch {
            bleService.nearByDevices.collect { devices ->
                _uiState.update {
                    it.copy(
                        advertisements = devices
                    )
                }
            }
        }
    }
}