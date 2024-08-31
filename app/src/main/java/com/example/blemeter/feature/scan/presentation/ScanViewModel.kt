package com.example.blemeter.feature.scan.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blemeter.config.extenstions.isConnected
import com.example.blemeter.core.ble.data.BLEService
import com.example.blemeter.core.ble.data.IBLEService
import com.example.blemeter.feature.scan.domain.model.ScanScreenStatus
import com.example.blemeter.feature.scan.domain.repository.IScanRepository
import com.juul.kable.AndroidAdvertisement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
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
    private val scanRepo: IScanRepository
) : ViewModel() {

    companion object {
        const val SCAN_DURATION_MILLIS = 10000L
    }

    private var scanJob : Job? = null

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

    }


    private fun onDeviceConnect() {
        _uiState.value.selectedDevice?.let { device ->
            viewModelScope.launch {
                scanRepo.initPeripheral(device)

                //observing the state only after initialising the peripheral
                observeConnectionState()

                //Making connection
                delay(100L)
                scanRepo.connect()
            }
        }
    }

    private fun observeConnectionState() {
        viewModelScope.launch {
            scanRepo.peripheral?.state?.collect { state ->
                Log.d("ConnectionMaking", "state: $state")
                _uiState.update {
                    it.copy(
                        screenStatus = ScanScreenStatus.OnConnection(state)
                    )
                }
            }
        }
    }

    //endregion Connection
}