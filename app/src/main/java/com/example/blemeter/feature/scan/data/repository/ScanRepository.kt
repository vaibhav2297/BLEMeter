package com.example.blemeter.feature.scan.data.repository

import com.example.blemeter.core.ble.data.IBLEService
import com.example.blemeter.feature.scan.domain.repository.IScanRepository
import com.juul.kable.AndroidAdvertisement
import com.juul.kable.Peripheral
import javax.inject.Inject

class ScanRepository @Inject constructor(
    private val bleService: IBLEService
) : IScanRepository {

    override val advertisement =
        bleService.scanner.advertisements

    override val peripheral: Peripheral? = null

    override fun initPeripheral(advertisement: AndroidAdvertisement) : Peripheral? =
        bleService.initPeripheral(advertisement)

    override suspend fun connect() = bleService.connect()

    override suspend fun disconnect() = bleService.disconnect()
}