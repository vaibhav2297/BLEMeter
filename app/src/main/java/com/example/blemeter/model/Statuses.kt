package com.example.blemeter.model

data class Statuses(
    val batteryState: BatteryVoltage = BatteryVoltage.UNKNOWN,
    val controlState: ControlState = NoneStatus.NONE
)
