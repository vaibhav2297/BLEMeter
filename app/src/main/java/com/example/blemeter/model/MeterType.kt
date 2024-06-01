package com.example.blemeter.model


sealed class MeterType(val code: Int) {

    sealed class WaterMeter(code: Int) : MeterType(code) {

        data object ColdWaterMeter : WaterMeter(16) //10H

        data object DomesticHotWaterMeter : WaterMeter(17) //11H

        data object DrinkingWaterMeter : WaterMeter(18) //12H

        data object MiddleWaterMeter : WaterMeter(19) //20H

        //General meter that used if none of above
        data object GeneralWaterMeter : WaterMeter(20)
    }

    sealed class HeatMeter(code: Int) : MeterType(code){

        data object ColdMeteringHeatMeter : HeatMeter(32) //20H

        data object HeatMeteringHeatMeter : HeatMeter(33) //21H

        //General meter that used if none of above
        data object GeneralHeatMeter : HeatMeter(34)
    }

    data object GasMeter : MeterType(48) //30-39H

    data object ElectricityMeter : MeterType(64) //40-49H

    data object Unknown : MeterType(-1)

    companion object {
        fun getMeterType(code: Byte) : MeterType {
            return when(code.toInt()) {
                in 16..25 -> when(code.toInt()) {
                    WaterMeter.ColdWaterMeter.code -> WaterMeter.ColdWaterMeter
                    WaterMeter.DomesticHotWaterMeter.code -> WaterMeter.DomesticHotWaterMeter
                    WaterMeter.DrinkingWaterMeter.code -> WaterMeter.DrinkingWaterMeter
                    WaterMeter.MiddleWaterMeter.code -> WaterMeter.MiddleWaterMeter
                    else -> WaterMeter.GeneralWaterMeter
                }
                in 32..41 -> when(code.toInt()) {
                    HeatMeter.ColdMeteringHeatMeter.code -> HeatMeter.ColdMeteringHeatMeter
                    HeatMeter.HeatMeteringHeatMeter.code -> HeatMeter.HeatMeteringHeatMeter
                    else -> HeatMeter.GeneralHeatMeter
                }
                in 48..57 -> GasMeter
                in 64..73 -> ElectricityMeter
                else -> Unknown
            }
        }
    }
}