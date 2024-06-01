package com.example.blemeter.feature.communication.domain.usecases

import com.example.blemeter.core.ble.data.repository.IBLERepository
import com.example.blemeter.core.ble.domain.bleparsable.Command
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.ble.domain.model.request.BaseRequest

open class BaseCommandUseCase(
    private val bleRepository: IBLERepository
) {
    @OptIn(ExperimentalUnsignedTypes::class)
    protected fun <T> writeCharacteristic(command: Command<T, *>, request: T): Result<Unit> = run {
        return try {
            bleRepository.deviceServices.value.let { svc ->
                val characteristic = svc.flatMap { it.characteristics }
                    .find { it.uuid == MeterServicesProvider.MainService.WRITE_CHARACTERISTIC }

                if (characteristic != null) {
                    //bleRepository.writeBytes(characteristic.uuid, command.toCommand(request))
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Write characteristic not found"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    protected fun readCharacteristics(uuid: String) {
        bleRepository.readCharacteristics(uuid)
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    protected fun getBaseRequest() : BaseRequest {
        var baseRequest = BaseRequest()
        bleRepository.deviceServices.value.let { svc ->
            svc.find { it.uuid == MeterServicesProvider.DeviceInfo.SERVICE }?.characteristics?.forEach { char ->
//                when (char.uuid) {
//
//                    MeterServicesProvider.DeviceInfo.MODEL_NUMBER -> {
//                        val meterAddress = MeterAddressCommand.fromCommand(
//                            command = char.readBytes ?: ubyteArrayOf()
//                        )
//
//                        baseRequest = baseRequest.copy(
//                            meterAddress = meterAddress,
//                            meterType = meterAddress.meterType
//                        )
//                    }
//
//                    MeterServicesProvider.DeviceInfo.SERIAL_NUMBER -> {
//                        baseRequest = baseRequest.copy(
//                            serialNumber = SerialNumberCommand.fromCommand(char.readBytes ?: ubyteArrayOf())
//                        )
//                    }
//                }
            }

            return baseRequest
        }
    }
}