package com.example.blemeter.feature.connection.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blemeter.config.extenstions.getMeterAddress
import com.example.blemeter.config.extenstions.isConnected
import com.example.blemeter.config.extenstions.isDisconnected
import com.example.blemeter.core.ble.data.BLEService
import com.example.blemeter.core.local.DataStore
import com.example.blemeter.core.logger.ExceptionHandler
import com.example.blemeter.core.logger.ILogger
import com.juul.kable.AndroidAdvertisement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnViewModel @Inject constructor(
    private val bleService: BLEService,
    private val dataStore: DataStore,
    private val logger: ILogger,
    private val exceptionHandler: ExceptionHandler
) : ViewModel() {

    private var _scanningJob: Job? = null

    private val _nearByDevices = mutableListOf<AndroidAdvertisement>()

    private var _selectedDevice: AndroidAdvertisement? = null

    private val _uiState = MutableStateFlow(ConnectionUiState())
    val uiState = _uiState.asStateFlow()

    init {
        //Use when handling Meter address manually
        /*checkMeterAddress()
        observeMeterAddress()*/
    }

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
            logger.d("Connecting device :: name :: ${advertisement.name} :: address :: ${advertisement.address}")
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

                //updating state to UI
                _uiState.update {
                    it.copy(state = state)
                }

                //saving meter address to preference on connection established
                if (state.isConnected()) {
                    extractAndSaveMeterAddress()
                }
            }
        }
    }

    private fun fetchDeviceInfo() {
        viewModelScope.launch {
            /*val deviceInfo = useCases.getDeviceInfoUseCase()
            logger.d("Connection VM :: Fetch Device Info $deviceInfo")
            _uiState.update { it.copy(isServiceDiscovered = true) }*/
        }
    }

    //Used when auto extract meter address
    private fun extractAndSaveMeterAddress() {
        try {
            val address = bleService.peripheral?.name?.getMeterAddress() ?: ""
            logger.d("extractAndSaveMeterAddress :: meter Address :: $address")
            saveMeterAddress(address)
        } catch (e:Exception) {
            exceptionHandler.handle(e)
        }
    }

    private fun saveMeterAddress(address: String) {
        viewModelScope.launch {
            dataStore.saveMeterAddress(address)
        }
    }

    //Used when manually get meter address
    private fun checkMeterAddress() {
        viewModelScope.launch {
            val isMeterAddressRequired = dataStore.getMeterAddress().first().isEmpty()

            _uiState.update {
                it.copy(isMeterAddressRequired = isMeterAddressRequired)
            }
        }
    }

    private fun observeMeterAddress() {
        viewModelScope.launch {
            dataStore.getMeterAddress().collect { address ->
                _uiState.update {
                    it.copy(isMeterAddressRequired = address.isEmpty())
                }
            }
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