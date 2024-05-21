package com.example.blemeter.model

data class MeterData(
    val accumulatedUsage: Int = 0,
    val surplus: Int = 0,
    val totalPurchase: Int = 0,
    val numberTimes: Int = 0,
    val statuses: Statuses = Statuses(),
    val alarmVariable: Int = 0,
    val overdraft: Int = 0,
    val minimumUsage: Int = 0,
    val additionDeduction: Int = 0,
    val productVersion: Int = 0,
    val programVersion: Int = 0,
) : Data
