package com.example.blemeter.model

import com.example.blemeter.core.ble.domain.model.DataIdentifier

data class MeterData(
    val accumulatedUsage: Double = 0.0,
    val surplus: Double = 0.0,
    val totalPurchase: Double = 0.0,
    val numberTimes: UInt = 0u,
    val statuses: Statuses = Statuses(),
    val alarmVariable: UInt = 0u,
    val overdraft: UInt = 0u,
    val minimumUsage: UInt = 0u,
    val additionDeduction: UInt = 0u,
    val productVersion: UInt = 0u,
    val programVersion: UInt = 0u,
) : Data {
    override val dataIdentifier: DataIdentifier
        get() = DataIdentifier.METER_DATA
}
