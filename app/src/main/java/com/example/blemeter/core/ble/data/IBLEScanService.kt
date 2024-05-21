package com.example.blemeter.core.ble.data

import com.example.blemeter.core.ble.domain.model.BleScanEvent
import kotlinx.coroutines.flow.StateFlow

interface IBLEScanService {

    val scanEvent: StateFlow<BleScanEvent>
    suspend fun scanLeDevice()
    fun stopLeScan()
}