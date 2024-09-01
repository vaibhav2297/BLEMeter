package com.example.blemeter.feature.communication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blemeter.core.ble.data.BLEService
import com.example.blemeter.core.ble.domain.bleparsable.PurchaseDataCommand
import com.example.blemeter.core.ble.domain.bleparsable.ReadMeterDataCommand
import com.example.blemeter.core.ble.domain.bleparsable.ValveControlCommand
import com.example.blemeter.core.ble.domain.model.DataIdentifier
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.ble.domain.model.request.AccumulateDataRequest
import com.example.blemeter.core.ble.domain.model.request.PurchaseDataRequest
import com.example.blemeter.core.ble.utils.BLEConstants
import com.example.blemeter.core.logger.ExceptionHandler
import com.example.blemeter.core.logger.ILogger
import com.example.blemeter.model.Data
import com.example.blemeter.model.MeterData
import com.example.blemeter.model.ValveControlData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunicationViewModel @Inject constructor(
    private val bleService: BLEService,
    private val logger: ILogger,
    private val exceptionHandler: ExceptionHandler
) : ViewModel() {

    private var _observeBLEResponseJob: Job? = null

    private val _uiState = MutableStateFlow(CommunicationUiState())
    val uiState = _uiState.asStateFlow()

    private val meterDataArray = mutableListOf<UByte>()

    init {
        observeConnectionState()
        observeBLEResponse()
    }

    fun onEvent(event: CommunicationUiEvent) {
        when (event) {
            is CommunicationUiEvent.OnMeterDataRead -> readMeterData()
            is CommunicationUiEvent.OnValveInteraction -> valveInteraction(event.valveInteractionCommand)
            is CommunicationUiEvent.OnPurchaseData -> writePurchaseData(event.request)
            is CommunicationUiEvent.OnZeroInitialise -> writeZeroInitialising()
            is CommunicationUiEvent.OnAccumulateData -> writeAccumulation(event.request)
        }
    }

    private fun observeConnectionState() {
        viewModelScope.launch {
            bleService.peripheral?.state?.collectLatest { state ->
                logger.d("status: $state")
                _uiState.update { it.copy(connectionState = state) }
            }
        }
    }

    //region Meter Data
    private fun readMeterData() {
        /*viewModelScope.launch {
            showLoading(true)
            useCases.readMeterDataUseCase()
                .onFailure { e ->
                    showLoading(false)
                    logger.d("readMeterData :: Failure :: ${e.message}")
                }
        }*/
    }
    //endregion Meter Data

    //region valve control
    private fun valveInteraction(status: com.example.blemeter.core.ble.domain.model.request.ValveInteractionCommand) {
        /*viewModelScope.launch {
            showLoading(true)
            logger.d("Valve interaction : ${status.name}")
            useCases.valveControlUseCase(status)
                .onFailure { e ->
                    showLoading(false)
                    logger.d("valveInteraction :: Failure :: ${e.message}")
                }
        }*/
    }


    //endregion valve valve control

    //region Purchase Data
    private fun writePurchaseData(request: PurchaseDataRequest) {
        /*viewModelScope.launch {
            logger.d("Purchase data")
            showLoading(true)
            useCases.purchaseDataUseCase(request)
                .onFailure { e ->
                    showLoading(false)
                    logger.d("purchaseData :: Failure :: ${e.message}")
                }
        }*/
    }
    //endregion Purchase Data

    override fun onCleared() {
        _observeBLEResponseJob?.cancel()
        _observeBLEResponseJob = null
        super.onCleared()
    }


    @OptIn(ExperimentalUnsignedTypes::class, ExperimentalStdlibApi::class)
    private fun observeBLEResponse() {
        _observeBLEResponseJob = viewModelScope.launch {
            bleService.observeCharacteristic(
                service = MeterServicesProvider.MainService.SERVICE,
                observeCharacteristic = MeterServicesProvider.MainService.NOTIFY_CHARACTERISTIC
            )?.map { response ->

                logger.d("observeBLEResponse :: Response :: ${response.contentToString()}")
                logger.d("observeBLEResponse :: Response :: ${response.toHexString()}")

                meterDataArray.addAll(response)

                if (response.last() == BLEConstants.EOF) {
                    /*val meterData = MeterDataCommand.fromCommand(meterDataArray.toUByteArray().toHexString())*/
                    logger.d("observeBLEResponse :: Complete command received")
                    val data = parseCommandAndCreateData(meterDataArray.toUByteArray().toHexString())
                    meterDataArray.clear()

                    showLoading(false)

                    return@map data
                } else {
                    return@map null
                }
            }?.catch {e ->
                logger.d("observeBLEResponse :: catch :: ${e.message}")
            }?.collect { data ->
                data?.let {
                    logger.d("observeBLEResponse :: Data : $data")
                    updateDataToUI(it)
                }
            }
        }
    }

    private fun updateDataToUI(data: Data) {
        when(data) {
            is MeterData -> {
                _uiState.update {
                    it.copy(meterData = data)
                }
            }
            is ValveControlData -> {
                _uiState.update {
                    it.copy(valveControlData = data)
                }
            }
        }
    }

    private fun parseCommandAndCreateData(value: String) : Data? {
        val dataIdentifier = DataIdentifier.getDataType(value)
        logger.d("parseCommandAndCreateData :: Data Type : ${dataIdentifier.name}")
        return try {
            return when(dataIdentifier) {
                DataIdentifier.METER_DATA -> ReadMeterDataCommand.fromCommand(value)
                DataIdentifier.VALVE_CONTROL_DATA -> ValveControlCommand.fromCommand(value)
                DataIdentifier.PURCHASE_DATA -> PurchaseDataCommand.fromCommand(value)
                else -> null
            }
        } catch (e: Exception) {
            exceptionHandler.handle(e)
            null
        }
    }

    private fun showLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    //region Zero Initialising
    private fun writeZeroInitialising() {
        /*viewModelScope.launch {
            logger.d("Zero Initialising")
            showLoading(true)
            useCases.zeroInitialisationUseCase()
                .onFailure { e ->
                    showLoading(false)
                    logger.d("Zero Initialising :: Failure :: ${e.message}")
                }
        }*/
    }
    //endregion Zero Initialising

    //region Accumulation
    private fun writeAccumulation(request: AccumulateDataRequest) {
        /*viewModelScope.launch {
            logger.d("Accumulation")
            showLoading(true)
            useCases.accumulateDataUseCase(request)
                .onFailure { e ->
                    showLoading(false)
                    logger.d("Accumulation :: Failure :: ${e.message}")
                }
        }*/
    }
    //endregion Accumulation
}