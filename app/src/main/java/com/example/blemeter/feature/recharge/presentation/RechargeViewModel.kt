package com.example.blemeter.feature.recharge.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blemeter.config.model.CalibrationIdentification
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.ble.domain.model.request.PurchaseDataRequest
import com.example.blemeter.feature.dashboard.domain.usecases.DashboardUseCases
import com.example.blemeter.config.model.MeterData
import com.example.blemeter.config.model.NoData
import com.example.blemeter.feature.dashboard.domain.usecases.ObserveDataUseCase
import com.example.designsystem.utils.ScreenState
import com.example.local.datastore.DataStoreKeys
import com.example.local.datastore.IAppDataStore
import com.example.logger.ExceptionHandler
import com.example.logger.ILogger
import com.example.wallet.domain.model.TransactionType
import com.example.wallet.domain.repository.WalletRepository
import com.example.meter.domain.model.request.MeterLogRequest
import com.example.meter.domain.repository.IMeterTransactionRepository
import com.example.wallet.domain.model.request.WalletTransactionRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RechargeViewModel @Inject constructor(
    private val useCases: DashboardUseCases,
    private val walletRepository: WalletRepository,
    private val meterTransactionRepository: IMeterTransactionRepository,
    private val observeDataUseCase: ObserveDataUseCase,
    private val dataStore: IAppDataStore,
    private val exceptionHandler: ExceptionHandler,
    private val logger: ILogger
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

            walletRepository
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

            //Cost Configuration
            val costConfiguration =
                dataStore.getPreference(DataStoreKeys.COST_CONFIGURATION_KEY, 0.0).firstOrNull()
                    ?: 0.0

            val meterCalibration =
                dataStore.getPreference(DataStoreKeys.METER_CALIBRATION_TYPE, 0).firstOrNull()
                    ?: 0

            val calibrationIdentification =
                CalibrationIdentification.getCalibrationIdentificationByCommandBit(meterCalibration.toUInt())

            val purchaseData =
                ((_uiState.value.rechargeAmount * costConfiguration) / calibrationIdentification.factor.toDouble()).coerceAtLeast(
                    0.0
                )

            //1 rs -- 100 litre
            //? rs -- 50 litre

            useCases.purchaseDataUseCase(
                request = PurchaseDataRequest(
                    numberTimes = rechargeTimes.inc(),
                    purchaseVariable = purchaseData
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
                exceptionHandler.handle(cause)
                _uiState.update {
                    it.copy(
                        screenState = ScreenState.Error(cause.message ?: "Unknown Error")
                    )
                }
            }?.collect { data ->
                logger.d("RechargeVM :: observerResponse :: $data")
                when (data) {
                    is MeterData -> {

                        val oldRechargeTimes =
                            dataStore.getPreference(DataStoreKeys.RECHARGE_TIMES_KEY, 0)
                                .firstOrNull() ?: 0

                        //indicates successful recharge
                        if ((oldRechargeTimes.inc()) == data.numberTimes.toInt()) {

                            //saving recharge times
                            saveRechargeTimes(data.numberTimes.toInt())

                            //log to the server
                            insertMeterLog(data)
                            insertWalletTransaction(data)
                        }

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

    private suspend fun saveRechargeTimes(numberOfTimes: Int) {
        logger.d("RechargeViewmodel: saveRechargeTimes: $numberOfTimes")
        dataStore.putPreference(DataStoreKeys.RECHARGE_TIMES_KEY, numberOfTimes)
    }

    private suspend fun insertWalletTransaction(data: MeterData) {

        val userId =
            dataStore.getPreference(DataStoreKeys.USER_ID_KEY, "").firstOrNull() ?: ""

        val meterId =
            dataStore.getPreference(DataStoreKeys.METER_ADDRESS_KEY, "").firstOrNull() ?: ""

        val walletId = walletRepository.getWalletId(userId)

        val request = WalletTransactionRequest(
            userId = userId,
            meterId = meterId,
            amount = _uiState.value.rechargeAmount,
            purchaseFrequency = data.numberTimes.toInt(),
            transactionType = TransactionType.DEBIT.name,
            walletId = walletId
        )

        logger.d("RechargeViewmodel: walletTransactionRequest: $request")

        walletRepository.insertWalletTransaction(request)
            .onSuccess { logger.d("RechargeViewmodel :: insertWalletTransaction: success") }
            .onFailure { e -> exceptionHandler.handle(Exception(e)) }
    }

    private suspend fun insertMeterLog(data: MeterData) {

        val userId =
            dataStore.getPreference(DataStoreKeys.USER_ID_KEY, "").firstOrNull() ?: ""

        val meterId =
            dataStore.getPreference(DataStoreKeys.METER_ADDRESS_KEY, "").firstOrNull() ?: ""

        val request = MeterLogRequest(
            userId = userId,
            meterId = meterId,
            minimumUsageVariable = data.minimumUsage.toInt(),
            inplaceMethod = data.productVersion.inPlaceMethod.name,
            calibrationIdentification = data.productVersion.calibrationIdentification.name,
            batteryVoltage = data.statuses.batteryState.name,
            alarmVariables = data.alarmVariable.toInt(),
            additionalDeductions = data.additionDeduction.toInt(),
            accumulatedUsage = data.accumulatedUsage,
            totalPurchase = data.totalPurchase,
            valveStatus = data.statuses.controlState.title(),
            overdraftVariables = data.overdraft.toInt(),
            surplusVariable = data.surplus,
            purchaseFrequency = data.numberTimes.toInt(),
            programVersion = data.programVersion.toInt(),
            paymentMethod = data.productVersion.paymentMethod.name
        )

        logger.d("RechargeViewmodel: insertMeterLog: $request")

        meterTransactionRepository.insertMeterLogs(request)
            .onSuccess { logger.d("RechargeViewmodel: insertMeterLogs: success") }
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