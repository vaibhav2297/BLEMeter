package com.example.blemeter.config.model

import com.example.blemeter.model.MeterType

data class MeterConfig(
    val meterAddress: String = "",
    val meterType: MeterType = MeterType.WaterMeter.DrinkingWaterMeter
)
