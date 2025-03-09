package com.example.blemeter.feature.dashboard.domain.usecases

import com.example.blemeter.core.ble.domain.command.PurchaseDataCommand
import com.example.blemeter.core.ble.domain.command.ReadMeterDataCommand
import com.example.blemeter.core.ble.domain.command.ValveControlCommand
import com.example.blemeter.core.ble.domain.model.DataIdentifier
import com.example.blemeter.config.constants.BLEConstants
import com.example.blemeter.config.constants.Logger
import com.example.blemeter.feature.dashboard.domain.repository.IDashboardRepository
import com.example.blemeter.config.model.Data
import com.example.blemeter.config.model.NoData
import com.example.blemeter.core.ble.domain.command.NumberingInstructionDataCommand
import com.example.blemeter.core.logger.ExceptionHandler
import com.example.blemeter.core.logger.ILogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@OptIn(ExperimentalUnsignedTypes::class, ExperimentalStdlibApi::class)
class ObserveDataUseCase @Inject constructor(
    private val repository: IDashboardRepository,
    private val exceptionHandler: ExceptionHandler
) {

    private val responseData = mutableListOf<UByte>()

    operator fun invoke(
        service: String,
        observeCharacteristic: String
    ): Flow<Data>? {
        return repository.observeCharacteristic(
            service = service,
            observeCharacteristic = observeCharacteristic
        )?.onEach { value ->
            responseData.addAll(value)
        }?.filter {
            responseData.lastOrNull() == BLEConstants.EOF
        }?.map {
            parseResponse(responseData.toUByteArray().toHexString())
        }?.onEach {
            responseData.clear()
        }
    }

    private fun parseResponse(value: String): Data {
        val dataIdentifier = DataIdentifier.getDataType(value)
        return try {
            return when (dataIdentifier) {
                DataIdentifier.METER_DATA -> ReadMeterDataCommand.fromCommand(value)
                DataIdentifier.VALVE_CONTROL_DATA -> ValveControlCommand.fromCommand(value)
                DataIdentifier.PURCHASE_DATA -> PurchaseDataCommand.fromCommand(value)
                DataIdentifier.NUMBERING_INSTRUCTION -> NumberingInstructionDataCommand.fromCommand(value)
                else -> NoData()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            exceptionHandler.handle(e)
            NoData()
        }
    }
}