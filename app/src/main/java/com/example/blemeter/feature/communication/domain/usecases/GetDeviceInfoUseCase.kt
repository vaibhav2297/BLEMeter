package com.example.blemeter.feature.communication.domain.usecases

import com.example.blemeter.core.ble.data.repository.BLEService
import com.example.blemeter.core.ble.data.repository.IBLERepository
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.model.DeviceInfo
import kotlinx.coroutines.delay
import javax.inject.Inject

class GetDeviceInfoUseCase @Inject constructor(
    private val bleService: BLEService
) {
    suspend operator fun invoke(): DeviceInfo {

        var deviceInfo = DeviceInfo()

        bleService.peripheral?.services
            ?.find { it.serviceUuid.toString() == MeterServicesProvider.DeviceInfo.SERVICE }
            ?.characteristics?.forEach { char ->
                val response = bleService.readCharacteristics(
                    service = char.serviceUuid.toString(),
                    characteristic = char.characteristicUuid.toString()
                )

                when (char.characteristicUuid.toString()) {
                    MeterServicesProvider.DeviceInfo.METER_ID -> {
                        deviceInfo = deviceInfo.copy(meterId = response?.decodeToString() ?: "")
                    }
                    MeterServicesProvider.DeviceInfo.MODEL_NUMBER -> {
                        val modelNumber = response?.decodeToString() ?: ""
                        deviceInfo = deviceInfo.copy(modelNumber = retrieveMeterAddress(modelNumber))
                    }
                    MeterServicesProvider.DeviceInfo.SERIAL_NUMBER -> {
                        deviceInfo = deviceInfo.copy(serialNumber = response?.decodeToString() ?: "")
                    }
                    MeterServicesProvider.DeviceInfo.FIRMWARE_REVISION -> {
                        deviceInfo = deviceInfo.copy(firmwareRevision = response?.decodeToString() ?: "")
                    }
                    MeterServicesProvider.DeviceInfo.HARDWARE_REVISION -> {
                        deviceInfo = deviceInfo.copy(hardwareRevision = response?.decodeToString() ?: "")
                    }
                    MeterServicesProvider.DeviceInfo.SOFTWARE_REVISION -> {
                        deviceInfo = deviceInfo.copy(softwareRevision = response?.decodeToString() ?: "")
                    }
                    MeterServicesProvider.DeviceInfo.MANUFACTURER_NAME -> {
                        deviceInfo = deviceInfo.copy(manufacturerName = response?.decodeToString() ?: "")
                    }
                    MeterServicesProvider.DeviceInfo.CERTIFICATION_DATA_LIST -> {
                        deviceInfo = deviceInfo.copy(certificationDataList = response?.decodeToString() ?: "")
                    }
                    MeterServicesProvider.DeviceInfo.PNP_ID -> {
                        deviceInfo = deviceInfo.copy(pnpId = response?.decodeToString() ?: "")
                    }
                }
                delay(100L)
            }

        bleService.setDeviceInfo(deviceInfo)

        return deviceInfo
    }

    private fun retrieveMeterAddress(value: String): String {
        return if(value.contains("-")) {
            value.substringBefore("-")
        } else {
            value
        }
    }
}