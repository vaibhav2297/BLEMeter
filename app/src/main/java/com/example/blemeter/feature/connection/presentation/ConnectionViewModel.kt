package com.example.blemeter.feature.connection.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blemeter.core.ble.data.repository.IBLERepository
import com.example.blemeter.core.ble.domain.model.DeviceDetail
import com.example.blemeter.core.ble.domain.model.ScannedDevice
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.ble.domain.model.hasConfigCharacteristic
import com.example.blemeter.feature.connection.domain.usecases.ConnectionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
class ConnectionViewModel @Inject constructor(
    private val bleRepository: IBLERepository,
    private val useCases: ConnectionUseCases
) : ViewModel() {

    private val _services = bleRepository.deviceServices
    private val _connectionState = bleRepository.connectionState
    private val _scanEvent = bleRepository.bleScanEvent

    /*Device that use has selected to connect with it*/
    private val _selectedDevice = MutableStateFlow<ScannedDevice?>(null)

    private val _uiState = MutableStateFlow(ConnectionUiState())
    val uiState = _uiState.asStateFlow()

    private val _deviceDetails =
        combine(
            _selectedDevice,
            _services,
            _connectionState
        ) { selectedDevice, deviceServices, connectionState ->
            selectedDevice?.let {
                DeviceDetail(
                    device = it,
                    services = deviceServices,
                    connectionState = connectionState,
                    isSupportConfig = deviceServices.hasConfigCharacteristic(MeterServicesProvider.MainService.CLIENT_CHARACTERISTIC_CONFIGURATION_DESCRIPTOR)
                )

            }
        }

//    val uiState = combine(
////        _uiState,
////        _deviceDetails,
////        _connectionState,
////        _scanEvent
////    ) { uiState, deviceDetail, connectionState, scanEvents ->
////        ConnectionUiState(
////            scannedDevices = scanEvents.scannedDevices,
////            isScanning = scanEvents.isScanning,
////            deviceDetail = deviceDetail,
////            connectionState = connectionState,
////            connectionError = scanEvents.error,
////            isBluetoothEnabled = scanEvents.isBluetoothEnabled,
////
////            showBluetoothEnableDialog = uiState.showBluetoothEnableDialog,
////            shouldRequestBluetoothPermission = uiState.shouldRequestBluetoothPermission,
////            isPermissionGranted = uiState.isPermissionGranted,
////            dialog = uiState.dialog
////        )
////    }.flowOn(Dispatchers.IO)
////        .stateIn(
////            viewModelScope,
////            SharingStarted.WhileSubscribed(),
////            ConnectionUiState()
////        )


    init {
        observeConnectionStatus()
        observeDeviceServices()
        observeScanEvents()
        observeDeviceDetail()
    }

    fun onEvent(event: ConnectionUiEvent) {
        when (event) {
            is ConnectionUiEvent.OnConnectionEstablish -> {
                if (isDeviceAlreadyConnected(event.device))
                    close()
                else
                    connectToDevice(event.device)
            }

            is ConnectionUiEvent.OnStartScan -> requestBluetoothPermission()
            is ConnectionUiEvent.OnStopScan -> stopLeScan()
            is ConnectionUiEvent.BluetoothEvent -> onBluetoothEvents(event)
        }
    }

    //region observers
    private fun observeDeviceServices() {
        viewModelScope.launch {
            _services.collectLatest { services ->
                if (services.isNotEmpty()) {
                    getDeviceInfo()
                }
            }
        }
    }

    private fun observeConnectionStatus() {
        viewModelScope.launch {
            _connectionState.collect { status ->
                _uiState.update {
                    it.copy(connectionState = status)
                }
            }
        }
    }

    private fun observeScanEvents() {
        viewModelScope.launch {
            _scanEvent.collect { events ->
                _uiState.update {
                    it.copy(
                        isScanning = events.isScanning,
                        isBluetoothEnabled = events.isBluetoothEnabled,
                        scannedDevices = events.scannedDevices,
                        connectionError = events.error
                    )
                }
            }
        }
    }

    private fun observeDeviceDetail() {
        viewModelScope.launch {
            _deviceDetails.collect { deviceDetail ->
                _uiState.update {
                    it.copy(
                        deviceDetail = deviceDetail
                    )
                }
            }
        }
    }
    //endregion observers

    private fun getDeviceInfo() {
        viewModelScope.launch {
            useCases.getDeviceInfoUseCase()
        }
    }

    private fun isDeviceAlreadyConnected(scannedDevice: ScannedDevice): Boolean {
        return _uiState.value.deviceDetail?.device?.address == scannedDevice.address
    }

    private fun scanLeDevices() {
        viewModelScope.launch {
            bleRepository.scanLeDevice()
        }
    }

    private fun stopLeScan() {
        viewModelScope.launch {
            bleRepository.stopLeScan()
        }
    }

    private fun connectToDevice(scannedDevice: ScannedDevice) {
        viewModelScope.launch {
            _selectedDevice.value = scannedDevice
            //stopping scan before making connection
            bleRepository.stopLeScan()
            bleRepository.connect(scannedDevice)
        }
    }

    private fun close() {
        viewModelScope.launch {
            _selectedDevice.value = null
            bleRepository.close()
        }
    }

    private fun onBluetoothEvents(events: ConnectionUiEvent.BluetoothEvent) {
        when (events) {
            is ConnectionUiEvent.BluetoothEvent.OnBluetoothEnableDismiss -> showDashboardUiDialog(
                ConnectionUiDialog.None
            )

            is ConnectionUiEvent.BluetoothEvent.OnBluetoothEnabled -> {
                scanLeDevices()
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
                    scanLeDevices()
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