package com.example.blemeter.feature.recharge.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.ble.domain.model.request.PurchaseDataRequest
import com.example.blemeter.core.ble.domain.model.request.ValveInteractionCommand
import com.example.blemeter.core.local.DataStore
import com.example.blemeter.feature.dashboard.domain.usecases.DashboardUseCases
import com.example.blemeter.model.MeterData
import com.example.blemeter.model.ValveControlData
import com.example.blemeter.model.ValveStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RechargeViewModel @Inject constructor(
    private val useCases: DashboardUseCases,
    private val dataStore: DataStore
) : ViewModel() {

    companion object {
        const val TAG = "RechargeViewModel"
    }

    init {
        observeResponse()
    }

    private val _uiState: MutableStateFlow<RechargeUiState> by lazy {
        MutableStateFlow(RechargeUiState())
    }
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: RechargeUiEvent) {
        when (event) {
            is RechargeUiEvent.OnRecharge -> onRecharge()
            is RechargeUiEvent.OnRechargeValueChanged -> updateRechargeValue(event.amount)
        }
    }

    private fun updateRechargeValue(amount: Double) {
        _uiState.update {
            it.copy(rechargeAmount = amount)
        }
    }

    private fun onRecharge() {
        viewModelScope.launch {
            val rechargeTimes = dataStore.getRechargeTimes().first().inc()

            useCases.purchaseDataUseCase(
                request = PurchaseDataRequest(
                    numberTimes = rechargeTimes,
                    purchaseVariable = _uiState.value.rechargeAmount
                )
            )
                .onFailure {  }
                .onSuccess {  }
        }
    }

    private fun observeResponse() {
        viewModelScope.launch {
            useCases.observeDataUseCase(
                service = MeterServicesProvider.MainService.SERVICE,
                observeCharacteristic = MeterServicesProvider.MainService.NOTIFY_CHARACTERISTIC
            )?.catch { cause ->

            }?.collect { data ->
                Log.e(TAG, "RechargeVM :: observerResponse :: $data")
                when (data) {
                    is MeterData -> {

                        //saving recharge times
                        saveRechargeTimes(data.numberTimes.toInt())
                    }
                }
            }
        }
    }

    private fun saveRechargeTimes(numberOfTimes: Int) {
        viewModelScope.launch {
            dataStore.saveRechargeTimes(numberOfTimes)
        }
    }
}