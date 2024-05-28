package com.example.blemeter.model

sealed class MeterType(val code: Int) {

    sealed interface WaterMeter {

        data object ColdWaterMeter : MeterType(16) //10H

        data object DomesticHotWaterMeter : MeterType(17) //11H

        data object DrinkingWaterMeter : MeterType(18) //12H

        data object MiddleWaterMeter : MeterType(19) //20H
    }

    sealed interface HeatMeter {

        data object ColdMeteringHeatMeter : MeterType(32) //20H

        data object HeatMeteringHeatMeter : MeterType(33) //21H
    }

    data object GasMeter : MeterType(48) //30H

    data object ElectricityMeter : MeterType(64) //40H

    data object Unknown : MeterType(-1)

    companion object {
        fun getMeterType(code: UByte) : MeterType {
            return when(code.toInt()) {
                WaterMeter.ColdWaterMeter.code -> WaterMeter.ColdWaterMeter
                WaterMeter.DomesticHotWaterMeter.code -> WaterMeter.DomesticHotWaterMeter
                WaterMeter.DrinkingWaterMeter.code -> WaterMeter.DrinkingWaterMeter
                WaterMeter.MiddleWaterMeter.code -> WaterMeter.MiddleWaterMeter
                HeatMeter.ColdMeteringHeatMeter.code -> HeatMeter.ColdMeteringHeatMeter
                HeatMeter.HeatMeteringHeatMeter.code -> HeatMeter.HeatMeteringHeatMeter
                GasMeter.code -> GasMeter
                ElectricityMeter.code -> ElectricityMeter
                else -> { throw Exception("no meter found associated with ${code.toInt()}") }
            }
        }
    }
}