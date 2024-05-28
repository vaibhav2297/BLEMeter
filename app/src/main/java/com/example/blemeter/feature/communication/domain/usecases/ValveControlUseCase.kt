package com.example.blemeter.feature.communication.domain.usecases

import com.example.blemeter.core.ble.data.repository.IBLERepository
import com.example.blemeter.core.ble.domain.bleparsable.ValveControlCommand
import com.example.blemeter.core.ble.domain.model.request.ValveControlCommandStatus
import com.example.blemeter.core.ble.domain.model.request.ValveControlRequest
import javax.inject.Inject

@OptIn(ExperimentalUnsignedTypes::class)
class ValveControlUseCase @Inject constructor(
    bleRepository: IBLERepository
) : BaseCommandUseCase(bleRepository) {
    operator fun invoke(status: ValveControlCommandStatus): Result<Unit> {
        val baseRequest = getBaseRequest()
        val request = ValveControlRequest(
            baseRequest = baseRequest,
            status = status
        )
        return writeCharacteristic(ValveControlCommand, request)
    }
}