package com.example.blemeter.feature.scan.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blemeter.config.extenstions.chunkAndReverseString
import com.example.blemeter.config.extenstions.getMeterAddress
import com.example.blemeter.config.extenstions.isConnected
import com.example.blemeter.core.local.DataStore
import com.example.blemeter.feature.dashboard.navigation.DashboardDestination
import com.example.blemeter.feature.scan.domain.model.ScanScreenStatus
import com.example.blemeter.feature.scan.domain.repository.IScanRepository
import com.example.blemeter.navigation.BLEMeterNavDestination
import com.juul.kable.AndroidAdvertisement
import com.juul.kable.Peripheral
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val scanRepo: IScanRepository,
    private val dataStore: DataStore
) : ViewModel() {

    companion object {
        const val SCAN_DURATION_MILLIS = 7500L
        const val TAG = "ScanModel"
    }

    private var scanJob : Job? = null

    private var connectionJob : Job? = null

    private val foundDevices = hashMapOf<String, AndroidAdvertisement>()

    private val _uiState : MutableStateFlow<ScanUiState> by lazy {
        MutableStateFlow(ScanUiState())
    }
    val uiState = _uiState.asStateFlow()


    fun onEvent(event: ScanUiEvent) {
        when(event) {
            is ScanUiEvent.OnScan -> onScan()
            is ScanUiEvent.OnScanCancel -> onScanCancel()
            is ScanUiEvent.OnConnectionCancel -> onConnectionCancel()
            is ScanUiEvent.OnDeviceSelect -> onDeviceSelect(event.device)
            is ScanUiEvent.OnConnect -> onDeviceConnect()
            is ScanUiEvent.OnNavigated -> {
                updateScreenState(ScanScreenStatus.None)
                navigateTo(null)
            }
        }
    }

    private fun updateScreenState(state: ScanScreenStatus) {
        _uiState.update {
            it.copy(
                screenStatus = state
            )
        }
    }

    //region Scan
    private fun onScanCancel() {
        scanJob?.cancel()
        updateScreenState(ScanScreenStatus.None)
    }

    private fun onScan() {
        scanJob = viewModelScope.launch {

            //Removing the selected device before scanning
            onDeviceSelect(null)

            withTimeoutOrNull(SCAN_DURATION_MILLIS) {
                scanRepo.advertisement
                    .onStart {
                        updateScreenState(ScanScreenStatus.Scanning)
                    }
                    .catch { cause ->  }
                    .onCompletion {
                        if (foundDevices.isEmpty()) {
                            updateScreenState(ScanScreenStatus.NoDeviceFound)
                        } else {
                            updateScreenState(ScanScreenStatus.OnDevicesFound(foundDevices.values.toList()))
                        }
                    }
                    .collect { devices ->
                        foundDevices[devices.address] = devices
                    }
            }
        }
    }

    //endregion Scan

    //region Connection
    private fun onDeviceSelect(device: AndroidAdvertisement?) {
        _uiState.update {
            it.copy(
                selectedDevice = device
            )
        }
    }

    private fun onConnectionCancel() {
        connectionJob?.cancel()
        updateScreenState(ScanScreenStatus.None)
    }


    private fun onDeviceConnect() {
        _uiState.value.selectedDevice?.let { device ->
            connectionJob = viewModelScope.launch {
                val peripheral = scanRepo.initPeripheral(device)

                //observing the state only after initialising the peripheral
                observeConnectionState(peripheral)

                //Making connection
                delay(100L)
                scanRepo.connect()
            }
        }
    }

    private fun observeConnectionState(peripheral: Peripheral?) {
        viewModelScope.launch {
           peripheral?.state?.collect { state ->
                _uiState.update {
                    it.copy(
                        screenStatus = ScanScreenStatus.OnConnection(state)
                    )
                }

               //storing the meter address on connection
               if (state.isConnected()) {
                   try {
                       saveMeterAddress(address = peripheral.name?.getMeterAddress() ?: "")
                       navigateTo(DashboardDestination)
                   } catch (e: Exception) {
                       e.printStackTrace()
                   }
               }
            }
        }
    }

    //endregion Connection

    /**
     * Save the meter address to the data store.
     * Meter address is stored in the reverse order of 2 chunks
     * e.x - 123456 will store as 563412
     */
    private fun saveMeterAddress(address: String) {
        viewModelScope.launch {
            dataStore.saveMeterAddress(address.chunkAndReverseString())
        }
    }

    private fun navigateTo(destination: BLEMeterNavDestination?) {
        _uiState.update {
            it.copy(navigateTo = destination)
        }
    }


    override fun onCleared() {
        super.onCleared()
        scanJob = null
        connectionJob = null
    }
}