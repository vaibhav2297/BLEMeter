package com.example.blemeter.core.ble.data

import android.bluetooth.le.ScanSettings
import android.util.Log
import com.benasher44.uuid.uuidFrom
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.logger.ExceptionHandler
import com.example.blemeter.core.logger.ILogger
import com.example.blemeter.feature.scan.presentation.ScanViewModel
import com.example.blemeter.model.DeviceInfo
import com.juul.kable.AndroidAdvertisement
import com.juul.kable.Filter
import com.juul.kable.ObsoleteKableApi
import com.juul.kable.Peripheral
import com.juul.kable.Scanner
import com.juul.kable.State
import com.juul.kable.WriteType
import com.juul.kable.characteristicOf
import com.juul.kable.logs.Hex
import com.juul.kable.logs.Logging
import com.juul.kable.peripheral
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ObsoleteKableApi::class)
class BLEService @Inject constructor(
    private val scope: CoroutineScope,
    private val logger: ILogger,
    private val exceptionHandler: ExceptionHandler
) : IBLEService {

    companion object {
        const val TAG = "BLEService"
        const val ADJUSTMENT_DELAY = 500L
    }

    private var _deviceInfo: DeviceInfo? = null

    var peripheral: Peripheral? = null

    override val scanner = Scanner {
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

    @OptIn(ExperimentalStdlibApi::class)
    override fun initPeripheral(advertisement: AndroidAdvertisement): Peripheral? {
        logger.d("Initial peripheral")
        peripheral = scope.peripheral(advertisement) {
            logging {
                level = Logging.Level.Data
                format = Logging.Format.Multiline
                data = Logging.DataProcessor { data, operation, serviceUuid, characteristicUuid, descriptorUuid ->

                    logger.d(
                        "\noperation : ${operation?.name} \n" +
                                "serviceUuid : ${serviceUuid?.toString()} \n" +
                                "characteristicUuid : ${characteristicUuid?.toString()} \n" +
                                "data : ${data.toHexString()}"
                    )
                    data.toHexString()
                }
                this.identifier
            }
            observationExceptionHandler { cause ->
                exceptionHandler.handle(cause)
            }
        }

        return peripheral
    }

    override suspend fun connect() {
        try {
            peripheral?.connect()
        } catch (e: Exception) {
            exceptionHandler.handle(e)
        }
    }

    override suspend fun disconnect() {
        try {
            peripheral?.disconnect()
            peripheral = null
            _deviceInfo = null
        } catch (e: Exception) {
            exceptionHandler.handle(e)
        }
    }

    override suspend fun readCharacteristics(
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

    @OptIn(ExperimentalUnsignedTypes::class)
    override suspend fun writeCharacteristics(
        service: String,
        writeCharacteristic: String,
        value: UByteArray
    ) {
        logger.d("Write characteristic :: UUID: $writeCharacteristic")

        val writeChar = characteristicOf(
            service = service,
            characteristic = writeCharacteristic
        )
        logger.d("Write characteristic :: Confirming device before writing char :: name :: ${peripheral?.name} :: state : ${peripheral?.state?.value}")
        peripheral?.write(writeChar, value.toByteArray(), WriteType.WithResponse)
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    override fun observeCharacteristic(
        service: String,
        observeCharacteristic: String
    ): Flow<UByteArray>? {
        val observeChar = characteristicOf(
            service = service,
            characteristic = observeCharacteristic,
        )

        return peripheral?.observe(observeChar)?.map { it.toUByteArray() }
    }

    fun setDeviceInfo(deviceInfo: DeviceInfo) {
        _deviceInfo = deviceInfo
    }

    fun getDeviceInfo(): DeviceInfo? = _deviceInfo

    override suspend fun connectAndWrite(onCharWrite: suspend () -> Unit) {
        val stateFlow = peripheral?.state ?: return
        val connectionJob = CoroutineScope(Dispatchers.IO).launch {
            stateFlow.collect { state ->
                logger.d("connectAndWrite :: observe connection $state")
                if (state == State.Connected) {
                    delay(ADJUSTMENT_DELAY)
                    onCharWrite()
                    cancel()
                }
            }
        }

        try {
            connect()
            connectionJob.join()
        } catch (e: Exception) {
            connectionJob.cancel()
            exceptionHandler.handle(e)
        }
    }
}