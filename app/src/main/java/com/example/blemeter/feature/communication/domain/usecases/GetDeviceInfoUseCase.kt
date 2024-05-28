package com.example.blemeter.feature.communication.domain.usecases

import com.example.blemeter.core.ble.data.repository.IBLERepository
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import kotlinx.coroutines.delay
import javax.inject.Inject

class GetDeviceInfoUseCase @Inject constructor(
    private val bleRepository: IBLERepository
) {
    suspend operator fun invoke() {

        bleRepository.deviceServices.value
            .find { it.uuid == MeterServicesProvider.DeviceInfo.SERVICE }
            ?.characteristics?.forEach { char ->
                bleRepository.readCharacteristics(char.uuid)
                delay(400L)
            }
    }
}