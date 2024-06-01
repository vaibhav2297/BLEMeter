package com.example.blemeter.feature.communication.domain.usecases

import com.example.blemeter.core.ble.data.repository.BLEService
import com.example.blemeter.core.ble.data.repository.IBLERepository
import com.example.blemeter.core.ble.domain.bleparsable.MeterDataCommand
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.ble.domain.model.request.BaseRequest
import com.example.blemeter.core.ble.domain.model.request.MeterDataRequest
import com.example.blemeter.core.ble.utils.fromHexToUByteArray
import com.example.blemeter.core.ble.utils.toHexString
import com.example.blemeter.core.logger.ExceptionHandler
import com.example.blemeter.core.logger.ILogger
import com.example.blemeter.core.logger.Logger
import com.example.blemeter.model.MeterAddress
import com.example.blemeter.model.MeterAddress.Companion.toMeterAddress
import com.example.blemeter.model.MeterData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReadMeterDataUseCase @Inject constructor(
    private val bleService: BLEService,
    private val exceptionHandler: ExceptionHandler,
    private val logger: ILogger
) {
    suspend operator fun invoke(request: MeterDataRequest) : Result<Boolean> {
        return try {
            val deviceInfo = bleService.getDeviceInfo()
            logger.d("ReadMeterDataUseCase :: deviceInfo :: $deviceInfo")

            val meterAddressBytes = deviceInfo?.modelNumber?.toByteArray() ?: byteArrayOf()
            logger.d("ReadMeterDataUseCase :: meterAddressBytes :: ${meterAddressBytes.contentToString()}")

            val meterAddress = meterAddressBytes.toMeterAddress()
            logger.d("ReadMeterDataUseCase :: meterAddress :: $meterAddress")

            val baseRequest = BaseRequest(
                meterAddress = meterAddress,
                meterType = meterAddress.meterType
            )
            logger.d("ReadMeterDataUseCase :: baseRequest :: $baseRequest")

            val fullRequest = request.copy(baseRequest = baseRequest)
            logger.d("ReadMeterDataUseCase :: fullRequest :: $fullRequest")

            val meterCommand = MeterDataCommand.toCommand(fullRequest)
            logger.d("ReadMeterDataUseCase :: meterCommand :: ${meterCommand.contentToString()}")
            logger.d("ReadMeterDataUseCase :: meterCommand :: ${meterCommand.toHexString()}")

            bleService.writeCharacteristics(
                service = MeterDataCommand.serviceUuid,
                writeCharacteristic = MeterDataCommand.characteristicUuid,
                value = meterCommand
            )

            Result.success(true)

        } catch (e: Exception) {
            exceptionHandler.handle(e)
            Result.failure(e)
        }
    }
}