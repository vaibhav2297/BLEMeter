package com.example.blemeter.feature.recharge.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.ble.domain.model.request.PurchaseDataRequest
import com.example.blemeter.core.local.DataStore
import com.example.blemeter.feature.dashboard.domain.usecases.DashboardUseCases
import com.example.blemeter.config.model.MeterData
import com.example.blemeter.config.model.NoData
import com.example.blemeter.feature.dashboard.domain.usecases.ObserveDataUseCase
import com.example.designsystem.utils.ScreenState
import com.example.local.datastore.DataStoreKeys
import com.example.local.datastore.IAppDataStore
import com.example.payments.domain.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

@HiltViewModel
class RechargeViewModel @Inject constructor(
    private val useCases: DashboardUseCases,
    private val paymentRepository: PaymentRepository,
    private val observeDataUseCase: ObserveDataUseCase,
    private val dataStore: IAppDataStore
) : ViewModel() {

    companion object {
        const val TAG = "RechargeViewModel"
    }

    private val _uiState: MutableStateFlow<RechargeUiState> by lazy {
        MutableStateFlow(RechargeUiState())
    }
    val uiState = _uiState.asStateFlow()

    init {
        observeResponse()
        currentWalletBalance()
    }

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

    private fun checkAvailableBalance(): Boolean {
        if (_uiState.value.rechargeAmount > _uiState.value.currentAmount) {
            _uiState.update {
                it.copy(
                    screenState = ScreenState.Error("Not enough balance")
                )
            }

            return false
        }

        return true
    }

    private fun currentWalletBalance() {
        viewModelScope.launch {
            val userId = dataStore.getPreference(DataStoreKeys.USER_ID_KEY, "").firstOrNull() ?: ""

            paymentRepository
                .getUserWalletBalance(userId)
                .collectLatest { amount ->

                    Log.d(TAG, "currentWalletBalance: current Amount :: $amount")
                    _uiState.update {
                        it.copy(
                            currentAmount = amount
                        )
                    }
                }
        }
    }

    private fun onRecharge() {
        viewModelScope.launch {
            showLoading()

            if (!checkAvailableBalance()) return@launch

            val rechargeTimes =
                dataStore.getPreference(DataStoreKeys.RECHARGE_TIMES_KEY, 0).firstOrNull() ?: 0

            useCases.purchaseDataUseCase(
                request = PurchaseDataRequest(
                    numberTimes = rechargeTimes.inc(),
                    purchaseVariable = _uiState.value.rechargeAmount
                )
            )
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            screenState = ScreenState.Error(e.message ?: "Unknown Error")
                        )
                    }
                }
                .onSuccess {}
        }
    }

    private fun observeResponse() {
        viewModelScope.launch {
            observeDataUseCase(
                service = MeterServicesProvider.MainService.SERVICE,
                observeCharacteristic = MeterServicesProvider.MainService.NOTIFY_CHARACTERISTIC
            )?.catch { cause ->
                _uiState.update {
                    it.copy(
                        screenState = ScreenState.Error(cause.message ?: "Unknown Error")
                    )
                }
            }?.collect { data ->
                Log.e(TAG, "RechargeVM :: observerResponse :: $data")
                when (data) {
                    is MeterData -> {

                        //saving recharge times
                        saveRechargeTimes(data.numberTimes.toInt())

                        //update balance
                        updateWalletBalance()

                        _uiState.update {
                            it.copy(
                                screenState = ScreenState.Success(Unit)
                            )
                        }
                    }

                    is NoData -> {
                        _uiState.update {
                            it.copy(
                                screenState = ScreenState.Error("No Data received from device")
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun updateWalletBalance() {
        val userId = dataStore.getPreference(DataStoreKeys.USER_ID_KEY, "").firstOrNull() ?: ""
        val walletAmount = _uiState.value.currentAmount
        val rechargedAmount = _uiState.value.rechargeAmount

        Log.d(
            TAG, "updateWalletBalance: \n" +
                    "userId: $userId \n" +
                    "walletAmount: $walletAmount \n" +
                    "rechargeAmount: $rechargedAmount"
        )

        paymentRepository.updateWalletAmount(
            userId = userId,
            amount = walletAmount - rechargedAmount
        )
    }

    private suspend fun saveRechargeTimes(numberOfTimes: Int) {
        dataStore.putPreference(DataStoreKeys.RECHARGE_TIMES_KEY, numberOfTimes)
    }

    private fun showLoading() {
        _uiState.update {
            it.copy(
                screenState = ScreenState.Loading
            )
        }
    }
}