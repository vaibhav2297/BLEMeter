package com.example.blemeter.feature.recharge.presentation

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.ble.domain.model.request.PurchaseDataRequest
import com.example.blemeter.core.local.DataStore
import com.example.blemeter.feature.dashboard.domain.usecases.DashboardUseCases
import com.example.blemeter.config.model.MeterData
import com.example.blemeter.config.model.NoData
import com.example.blemeter.core.logger.ExceptionHandler
import com.example.blemeter.feature.dashboard.domain.usecases.ObserveDataUseCase
import com.example.designsystem.utils.ScreenState
import com.example.local.datastore.DataStoreKeys
import com.example.local.datastore.IAppDataStore
import com.example.payments.domain.model.TransactionType
import com.example.payments.domain.repository.PaymentRepository
import com.example.meter.domain.model.request.MeterLogRequest
import com.example.meter.domain.model.request.MeterTransactionRequest
import com.example.meter.domain.repository.IMeterTransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RechargeViewModel @Inject constructor(
    private val useCases: DashboardUseCases,
    private val paymentRepository: PaymentRepository,
    private val meterTransactionRepository: IMeterTransactionRepository,
    private val observeDataUseCase: ObserveDataUseCase,
    private val dataStore: IAppDataStore,
    private val exceptionHandler: ExceptionHandler
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

                        //log to the server
                        insertMeterTransaction(data)

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

        paymentRepository.updateWalletAmount(
            userId = userId,
            amount = _uiState.value.rechargeAmount,
            transactionType = TransactionType.DEBIT
        )
    }

    private suspend fun saveRechargeTimes(numberOfTimes: Int) {
        dataStore.putPreference(DataStoreKeys.RECHARGE_TIMES_KEY, numberOfTimes)
    }

    private suspend fun insertMeterTransaction(data: MeterData) {

        val userId =
            dataStore.getPreference(DataStoreKeys.USER_ID_KEY, "").firstOrNull() ?: ""

        val meterId =
            dataStore.getPreference(DataStoreKeys.METER_ADDRESS_KEY, "").firstOrNull() ?: ""

        val request = MeterTransactionRequest(
            userId = userId,
            meterId = meterId,
            amount = _uiState.value.rechargeAmount,
            purchaseTimes = data.numberTimes.toInt()
        )

        meterTransactionRepository.insertMeterTransaction(request)
            .onSuccess { Log.d(TAG, "insertMeterTransaction: success") }
            .onFailure { e -> exceptionHandler.handle(Exception(e)) }
    }

    private fun showLoading() {
        _uiState.update {
            it.copy(
                screenState = ScreenState.Loading
            )
        }
    }
}