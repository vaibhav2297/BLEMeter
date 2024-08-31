package com.example.blemeter.feature.scan.domain.repository

import com.juul.kable.AndroidAdvertisement
import com.juul.kable.Peripheral
import kotlinx.coroutines.flow.Flow

interface IScanRepository {
    val advertisement: Flow<AndroidAdvertisement>
    fun initPeripheral(advertisement: AndroidAdvertisement)
    suspend fun connect()
    suspend fun disconnect()
    val peripheral: Peripheral?
}