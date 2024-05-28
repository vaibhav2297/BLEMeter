package com.example.blemeter.core.ble.data.repository

import android.bluetooth.le.ScanSettings
import android.util.Log
import com.benasher44.uuid.uuidFrom
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.juul.kable.AndroidAdvertisement
import com.juul.kable.Filter
import com.juul.kable.ObsoleteKableApi
import com.juul.kable.Peripheral
import com.juul.kable.Scanner
import com.juul.kable.characteristicOf
import com.juul.kable.logs.Hex
import com.juul.kable.logs.Logging
import com.juul.kable.peripheral
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update

@OptIn(ObsoleteKableApi::class)
class BLEService(
    private val scope: CoroutineScope
) {

    companion object {
        const val TAG = "BLEService"
    }

    private val foundDevices = mutableSetOf<AndroidAdvertisement>()
    private val _nearByDevices = MutableStateFlow(foundDevices)
    val nearByDevices = _nearByDevices.asStateFlow()

    private var peripheral: Peripheral? = null

    private val scanner = Scanner {
        scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
            .build()
//        filters = listOf(
//            Filter.Service(uuidFrom(MeterServicesProvider.MainService.SERVICE))
//        )
        logging {
            level = Logging.Level.Data
            format = Logging.Format.Multiline
            data = Hex
        }
    }

    //scanning only stops when flow collection is terminated
    suspend fun observeAdvertisements() {
        scanner.advertisements.collect { adv ->
            foundDevices.add(adv)
            _nearByDevices.update { foundDevices }
            Log.d(TAG, "observeAdvertisements :: ${adv.address}")
        }
    }

    fun cleanNearByDevices() {
        foundDevices.clear()
        _nearByDevices.update { foundDevices }
    }

    fun getPeripheral(advertisement: AndroidAdvertisement) {
        peripheral = scope.peripheral(advertisement) {
            logging {
                level = Logging.Level.Data
                format = Logging.Format.Multiline
                data = Hex
            }

            onServicesDiscovered {

            }

            observationExceptionHandler { cause ->

            }
        }
    }

    suspend fun connect() {
        peripheral?.connect()
    }

    suspend fun observePeripheralState() {
        peripheral?.let { p ->
            p.state.collectLatest { state ->

            }
        }
    }

    suspend fun readCharacteristics(
        service: String,
        characteristic: String
    ) {
        val char = characteristicOf(
            service = service,
            characteristic = characteristic
        )

        peripheral?.read(char)

        peripheral?.observe(char)?.collect {

        }
    }
}