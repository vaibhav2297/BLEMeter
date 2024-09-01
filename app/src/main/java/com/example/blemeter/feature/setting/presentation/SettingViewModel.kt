package com.example.blemeter.feature.setting.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blemeter.core.ble.data.repository.IBLERepository
import com.example.blemeter.core.ble.domain.model.DeviceDetail
import com.example.blemeter.core.ble.domain.model.ScannedDevice
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.ble.domain.model.hasConfigCharacteristic
import com.example.blemeter.core.local.DataStore
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
class SettingViewModel @Inject constructor(
    private val dataStore: DataStore
) : ViewModel() {

    private val _uiState: MutableStateFlow<SettingUiState> by lazy {
        MutableStateFlow(SettingUiState())
    }
    val uiState = _uiState.asStateFlow()

    init {
        getMeterAddress()
    }

    fun onEvent(event: SettingUiEvent) {
        when (event) {
            is SettingUiEvent.OnMeterAddressEdit -> editMeterAddress(event.isEditing)
            is SettingUiEvent.OnMeterAddressSave -> saveMeterAddress(event.address)
            is SettingUiEvent.OnDialog -> openDialog(event.dialog)
        }
    }

    private fun openDialog(dialog: SettingUiDialog) {
        _uiState.update {
            it.copy(
                dialog = dialog
            )
        }
    }

    private fun editMeterAddress(isEditing: Boolean) {
        _uiState.update {
            it.copy(isEditing = isEditing)
        }
    }

    private fun saveMeterAddress(address: String) {
        viewModelScope.launch {
            dataStore.saveMeterAddress(address)
        }
    }

    private fun getMeterAddress() {
        viewModelScope.launch {
            dataStore.getMeterAddress().collect { address ->
                _uiState.update {
                    it.copy(meterAddress = address)
                }
            }
        }
    }
}