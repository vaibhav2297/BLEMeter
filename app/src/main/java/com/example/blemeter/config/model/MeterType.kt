package com.example.blemeter.config.model


sealed class MeterType(val code: UInt) {

    sealed class WaterMeter(code: UInt) : MeterType(code) {

        data object ColdWaterMeter : WaterMeter(0x10u) //10H

        data object DomesticHotWaterMeter : WaterMeter(0x11u) //11H

        data object DrinkingWaterMeter : WaterMeter(0x12u) //12H

        data object MiddleWaterMeter : WaterMeter(0x13u) //13H

        //General meter that used if none of above
        data object GeneralWaterMeter : WaterMeter(0x14u)
    }

    sealed class HeatMeter(code: UInt) : MeterType(code){

        data object ColdMeteringHeatMeter : HeatMeter(0x20u) //20H

        data object HeatMeteringHeatMeter : HeatMeter(0x21u) //21H

        //General meter that used if none of above
        data object GeneralHeatMeter : HeatMeter(0x22u)
    }

    data object GasMeter : MeterType(0x30u) //30-39H

    data object ElectricityMeter : MeterType(0x40u) //40-49H

    data object Unknown : MeterType(0xFFFFFFFFu)

    companion object {
        fun getMeterType(code: UByte) : MeterType {
            return when(code.toUInt()) {
                in 16u..25u -> when(code.toUInt()) {
                    WaterMeter.ColdWaterMeter.code -> WaterMeter.ColdWaterMeter
                    WaterMeter.DomesticHotWaterMeter.code -> WaterMeter.DomesticHotWaterMeter
                    WaterMeter.DrinkingWaterMeter.code -> WaterMeter.DrinkingWaterMeter
                    WaterMeter.MiddleWaterMeter.code -> WaterMeter.MiddleWaterMeter
                    else -> WaterMeter.GeneralWaterMeter
                }
                in 32u..41u -> when(code.toUInt()) {
                    HeatMeter.ColdMeteringHeatMeter.code -> HeatMeter.ColdMeteringHeatMeter
                    HeatMeter.HeatMeteringHeatMeter.code -> HeatMeter.HeatMeteringHeatMeter
                    else -> HeatMeter.GeneralHeatMeter
                }
                in 48u..57u -> GasMeter
                in 64u..73u -> ElectricityMeter
                else -> Unknown
            }
        }
    }
}