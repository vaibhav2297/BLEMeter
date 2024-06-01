package com.example.blemeter.feature.connection.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blemeter.config.isDisconnected
import com.example.blemeter.core.ble.data.repository.BLEService
import com.example.blemeter.core.ble.domain.model.isDisconnected
import com.example.blemeter.core.logger.ILogger
import com.example.blemeter.core.logger.Logger
import com.example.blemeter.feature.connection.domain.usecases.ConnectionUseCases
import com.juul.kable.AndroidAdvertisement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnViewModel @Inject constructor(
    private val bleService: BLEService,
    private val useCases: ConnectionUseCases,
    private val logger: ILogger
) : ViewModel() {

    private var _scanningJob: Job? = null

    private val _nearByDevices = mutableListOf<AndroidAdvertisement>()

    private var _selectedDevice: AndroidAdvertisement? = null

    private val _uiState = MutableStateFlow(ConnectionUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: ConnectionUiEvent) {
        when (event) {
            is ConnectionUiEvent.OnConnectionEstablish -> {
                if (uiState.value.state.isDisconnected()) {
                    connect(event.device)
                } else {
                    disconnect()
                }
            }
            is ConnectionUiEvent.OnStartScan -> requestBluetoothPermission()
            is ConnectionUiEvent.OnStopScan -> stopScanning()
            is ConnectionUiEvent.BluetoothEvent -> onBluetoothEvents(event)
            is ConnectionUiEvent.OnNavigated -> {
                _uiState.update { it.copy(isServiceDiscovered = false) }
            }
        }
    }

    private fun startScan() {
        viewModelScope.launch {
            logger.d("Starting scan....")
            startScanning()
            delay(8000L)
            stopScanning()
        }
    }

    private fun startScanning() {
        _scanningJob = viewModelScope.launch {
            _nearByDevices.clear()
            _uiState.update {
                it.copy(
                    isScanning = true,
                    advertisements = _nearByDevices
                )
            }
            bleService.scanner.advertisements.collect { adv ->
                val isPresent = _nearByDevices.any { it.address == adv.address }
                if (!isPresent) {
                    logger.d("Device added => name : ${adv.name}  address: ${adv.address}")
                    _nearByDevices.add(adv)
                    _uiState.update { it.copy(advertisements = _nearByDevices) }
                }
            }
        }
    }

    private fun stopScanning() {
        logger.d("Stopping scan....")
        _scanningJob?.cancel()
        _uiState.update {
            it.copy(
                isScanning = false
            )
        }
    }

    private fun connect(advertisement: AndroidAdvertisement) {
        viewModelScope.launch {
            logger.d("Connecting device :: ${advertisement.address}")
            bleService.apply {
                stopScanning()
                _selectedDevice = advertisement
                _uiState.update { it.copy(connectedDevice = _selectedDevice) }
                initPeripheral(advertisement)
                observeConnectionState()
                connect()
                delay(400)
                fetchDeviceInfo()
            }
        }
    }

    private fun disconnect() {
        viewModelScope.launch {
            logger.d("Disconnecting device")
            bleService.disconnect()
            _selectedDevice = null
            _uiState.update { it.copy(connectedDevice = _selectedDevice) }
        }
    }

    private fun observeConnectionState() {
        viewModelScope.launch {
            bleService.peripheral?.state?.collect { state ->
                logger.d("Connection VM :: Connection Status :: $state")
                _uiState.update {
                    it.copy(state = state)
                }
            }
        }
    }

    private fun fetchDeviceInfo() {
        viewModelScope.launch {
            val deviceInfo = useCases.getDeviceInfoUseCase()
            logger.d("Connection VM :: Fetch Device Info $deviceInfo")
            _uiState.update { it.copy(isServiceDiscovered = true) }
        }
    }

    private fun onBluetoothEvents(events: ConnectionUiEvent.BluetoothEvent) {
        when (events) {
            is ConnectionUiEvent.BluetoothEvent.OnBluetoothEnableDismiss -> showDashboardUiDialog(
                ConnectionUiDialog.None
            )

            is ConnectionUiEvent.BluetoothEvent.OnBluetoothEnabled -> {
                startScan()
                showDashboardUiDialog(ConnectionUiDialog.None)
            }

            is ConnectionUiEvent.BluetoothEvent.OnPermissionResult -> {
                /* if permission is granted, assigning false to shouldRequestBluetoothPermission else altering its value to trigger state again */
                _uiState.update { state ->
                    state.copy(
                        shouldRequestBluetoothPermission = if (events.isGranted) false
                        else !state.shouldRequestBluetoothPermission,
                        isPermissionGranted = events.isGranted
                    )
                }

                if (events.isGranted) {
                    startScan()
                }
            }
        }
    }

    private fun showDashboardUiDialog(dialog: ConnectionUiDialog) {
        _uiState.update {
            it.copy(dialog = dialog)
        }
    }

    private fun requestBluetoothPermission() {
        _uiState.update { state ->
            state.copy(
                shouldRequestBluetoothPermission = true
            )
        }
    }
}