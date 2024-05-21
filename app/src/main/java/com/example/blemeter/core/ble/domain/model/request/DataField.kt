package com.example.blemeter.core.ble.domain.model.request

import com.example.blemeter.model.Data

data class DataField(
    val dataIdentification: Int,    // 2 byte command
    val serialNumber: Int,          // 1 byte command
    val data: Data
)
