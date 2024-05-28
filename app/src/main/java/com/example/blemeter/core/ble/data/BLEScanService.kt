package com.example.blemeter.core.ble.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.os.ParcelUuid
import android.util.Log
import com.example.blemeter.core.ble.domain.model.BleScanEvent
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.ble.domain.model.ScannedDevice
import com.example.blemeter.core.ble.domain.model.toScannedDevice
import com.example.blemeter.core.logger.ExceptionHandler
import com.example.blemeter.core.logger.ILogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@SuppressLint("MissingPermission")
class BLEScanService @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter,
    private val logger: ILogger,
    private val exceptionHandler: ExceptionHandler
) : IBLEScanService {

    companion object {
        const val SCAN_PERIOD: Long = 10000
        const val TAG = "BLEScanService"
    }

    private val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner

    private var isScanning = false

    private val scannedDevices = mutableListOf<ScannedDevice>()

    private val _event: MutableStateFlow<BleScanEvent> by lazy {
        MutableStateFlow(BleScanEvent())
    }
    override val scanEvent = _event.asStateFlow()

    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    private val scanFilter = ScanFilter
        .Builder()
        .setServiceUuid(ParcelUuid(MeterServicesProvider.getUUIDFor(MeterServicesProvider.MainService.SERVICE)))
        .build()

    private fun isBluetoothEnabled() = bluetoothAdapter.isEnabled

    override suspend fun scanLeDevice() {
        try {
            if (!bluetoothAdapter.isEnabled) {
                _event.update { it.copy(isBluetoothEnabled = false) }
                logger.e("bluetooth is not enabled")
                return
            }

            if (!isScanning) {
                // Removing already existing device list
                _event.update { it.copy(scannedDevices = listOf()) }
                scannedDevices.clear()
                startLeScan()
                delay(SCAN_PERIOD)
                stopLeScan()
            } else {
                logger.d("bluetooth already scanning")
                stopLeScan()
            }
        } catch (e: Exception) {
            exceptionHandler.handle(e)
            if (isScanning) stopLeScan()
        }
    }

    private fun startLeScan() {
        try {
            if (isBluetoothEnabled()) {
                logger.d("start scanning...")
                bluetoothLeScanner.startScan(listOf(scanFilter), scanSettings, leScanCallback)
                isScanning = true
                _event.update {
                    it.copy(
                        isScanning = true,
                        error = null
                    )
                }

            } else {
                logger.e("Bluetooth is not enabled")
                _event.update {
                    it.copy(
                        isScanning = false,
                        error = "Bluetooth is not enabled"
                    )
                }
            }
        } catch (e: Exception) {
            exceptionHandler.handle(e)
            _event.update {
                it.copy(
                    isScanning = false,
                    error = e.message
                )
            }
        }
    }

    override fun stopLeScan() {
        try {
            if (isBluetoothEnabled()) {
                logger.d("stop scanning")
                bluetoothLeScanner.stopScan(leScanCallback)
            } else {
                logger.d("bluetooth is not enabled")
            }
        } catch (e: Exception) {
            exceptionHandler.handle(e)
            _event.update {
                it.copy(
                    error = e.message
                )
            }
        } finally {
            logger.d("finally block")
            isScanning = false
            _event.update {
                it.copy(
                    isScanning = false
                )
            }
        }
    }

    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)

            logger.d("device info => name : ${result?.device?.name}, address : ${result?.device?.address} \n")

            result?.let { scanResult ->
                val scannedDevice = scanResult.toScannedDevice()

                //adding scan result to list if not added before
                val presentDevice = scannedDevices.find { it.address == result.device?.address }
                if (presentDevice == null) {

                    logger.d("Adding device to list : ${scannedDevice.deviceName}")
                    scannedDevices.add(scannedDevice)

                    _event.update {
                        it.copy(
                            scannedDevices = scannedDevices
                        )
                    }
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            logger.e("errorCode : $errorCode")

            //if scan is already started
            if (errorCode == 1) {
                logger.e("Scan already started")
                stopLeScan()
                startLeScan()
            }
        }
    }

}