package com.example.blemeter.feature.dashboard.domain.repository

import kotlinx.coroutines.flow.Flow

interface IDashboardRepository {
    suspend fun connectAndWrite(service: String, writeCharacteristic: String, value: UByteArray)
    suspend fun readCharacteristics(service: String, characteristic: String): ByteArray?
    fun observeCharacteristic(service: String, observeCharacteristic: String): Flow<UByteArray>?
}