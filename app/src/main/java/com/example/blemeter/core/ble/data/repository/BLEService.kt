package com.example.blemeter.core.ble.data.repository

import android.bluetooth.le.ScanSettings
import com.benasher44.uuid.uuidFrom
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.ble.utils.toHexString
import com.example.blemeter.core.logger.ExceptionHandler
import com.example.blemeter.core.logger.ILogger
import com.example.blemeter.model.DeviceInfo
import com.juul.kable.AndroidAdvertisement
import com.juul.kable.Filter
import com.juul.kable.ObsoleteKableApi
import com.juul.kable.Peripheral
import com.juul.kable.Scanner
import com.juul.kable.WriteType
import com.juul.kable.characteristicOf
import com.juul.kable.logs.Hex
import com.juul.kable.logs.Logging
import com.juul.kable.peripheral
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@OptIn(ObsoleteKableApi::class)
class BLEService(
    private val scope: CoroutineScope,
    private val logger: ILogger,
    private val exceptionHandler: ExceptionHandler
) {

    companion object {
        const val TAG = "BLEService"
    }

    private var _deviceInfo: DeviceInfo? = null

    var peripheral: Peripheral? = null

    val scanner = Scanner {
        scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
            .build()
        filters = listOf(
            Filter.Service(uuidFrom(MeterServicesProvider.MainService.SERVICE))
        )
        logging {
            level = Logging.Level.Data
            format = Logging.Format.Multiline
            data = Hex
        }
    }

    fun initPeripheral(advertisement: AndroidAdvertisement) {
        logger.d("Initial peripheral")
        peripheral = scope.peripheral(advertisement) {
            logging {
                level = Logging.Level.Data
                format = Logging.Format.Multiline
                data = Logging.DataProcessor { data, operation, serviceUuid, characteristicUuid, descriptorUuid ->

                    logger.d(
                        "operation : ${operation?.name} \n" +
                                "serviceUuid : ${serviceUuid?.toString()} \n" +
                                "characteristicUuid : ${characteristicUuid?.toString()} \n" +
                                "data : ${data.toHexString()}"
                    )
                    data.toHexString()
                }
            }
            observationExceptionHandler { cause ->
                exceptionHandler.handle(cause)
            }
        }
    }

    suspend fun connect() {
        try {
            peripheral?.connect()
        } catch (e: Exception) {
            exceptionHandler.handle(e)
        }
    }

    suspend fun disconnect() {
        try {
            peripheral?.disconnect()
            peripheral = null
            _deviceInfo = null
        } catch (e: Exception) {
            exceptionHandler.handle(e)
        }
    }

    suspend fun readCharacteristics(
        service: String,
        characteristic: String
    ): ByteArray? {
        logger.d("Read characteristic :: UUID: $characteristic")
        val char = characteristicOf(
            service = service,
            characteristic = characteristic
        )

        return peripheral?.read(char)
    }

    suspend fun writeCharacteristics(
        service: String,
        writeCharacteristic: String,
        value: ByteArray
    ) {
        logger.d("Write characteristic :: UUID: $writeCharacteristic")

        val writeChar = characteristicOf(
            service = service,
            characteristic = writeCharacteristic
        )

        peripheral?.write(writeChar, value)

        //return peripheral?.observe(observeChar)
    }

    fun observeCharacteristic(
        service: String,
        observeCharacteristic: String
    ): Flow<ByteArray>? {
        val observeChar = characteristicOf(
            service = service,
            characteristic = observeCharacteristic,
        )

        return peripheral?.observe(observeChar)
    }

    fun setDeviceInfo(deviceInfo: DeviceInfo) {
        _deviceInfo = deviceInfo
    }

    fun getDeviceInfo(): DeviceInfo? = _deviceInfo
}