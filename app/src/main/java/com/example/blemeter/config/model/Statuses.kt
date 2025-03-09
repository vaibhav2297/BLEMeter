package com.example.blemeter.config.model

data class Statuses(
    val batteryState: BatteryVoltage = BatteryVoltage.UNKNOWN,
    val controlState: ControlState = NoneStatus.NONE
)
