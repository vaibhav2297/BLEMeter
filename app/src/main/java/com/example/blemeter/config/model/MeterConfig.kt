package com.example.blemeter.config.model

data class MeterConfig(
    val meterAddress: String = "",
    val meterType: MeterType = MeterType.WaterMeter.DrinkingWaterMeter
)
