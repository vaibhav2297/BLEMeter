package com.example.blemeter.feature.dashboard.domain.usecases

import com.example.blemeter.core.ble.domain.bleparsable.PurchaseDataCommand
import com.example.blemeter.core.ble.domain.bleparsable.ReadMeterDataCommand
import com.example.blemeter.core.ble.domain.bleparsable.ValveControlCommand
import com.example.blemeter.core.ble.domain.model.DataIdentifier
import com.example.blemeter.core.ble.utils.BLEConstants
import com.example.blemeter.feature.dashboard.domain.repository.IDashboardRepository
import com.example.blemeter.model.Data
import com.example.blemeter.model.NoData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@OptIn(ExperimentalUnsignedTypes::class, ExperimentalStdlibApi::class)
class ObserveDataUseCase @Inject constructor(
    private val repository: IDashboardRepository
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
                else -> NoData()
            }
        } catch (e: Exception) {
            NoData()
        }
    }
}