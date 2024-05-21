package com.example.blemeter.core.ble.domain.bleparsable

import android.util.Log
import com.example.blemeter.core.ble.domain.model.request.ValveControlRequest
import com.example.blemeter.core.ble.utils.BLEConstants
import com.example.blemeter.model.MeterType
import com.example.blemeter.model.ValveControlData
import com.example.blemeter.core.ble.utils.fromHexToUByteArray

@ExperimentalUnsignedTypes
object ValveControlCommand : Command<ValveControlRequest, ValveControlData>(BLEConstants.sendId) {

    override val controlCode: Int = 4

    override val requestLength: Int = 7

    override fun toCommand(request: ValveControlRequest): UByteArray {

        //uByte array to hold bytes before check code
        // for accumulate total byte
        val arr = ubyteArrayOf(
            BLEConstants.SOF,
            MeterType.WaterMeter.ColdWaterMeter.code.toUByte(),
            *BLEConstants.METER_ADDRESS.fromHexToUByteArray(),
            controlCode.toUByte(),
            requestLength.toUByte(),
            160.toUByte(),                          //90H - data identification low byte
            23.toUByte(),                           //1FH - data identification high byte
            0u.toUByte(),                            //00F - serial number
            request.status.code.toUByte(),          // Open 055H  Close 99H
            BLEConstants.STAND_BY,
            BLEConstants.STAND_BY,
            BLEConstants.STAND_BY
        )

        return ubyteArrayOf(
            *arr,
            checkCode(arr), // accumulate total bytes
            BLEConstants.EOF
        )
    }

    override fun fromCommand(command: UByteArray): ValveControlData {
        try {
            require( command.size > 18 ) { "Expected Valve Control Data response size should be of 19 bytes. but it is ${command.size}" }

            command.run {

                return ValveControlData()
            }
        } catch (e: Exception) {
            Log.e("command", "ValveControlCommand :: fromCommand: ${e.message}")
            return ValveControlData()
        }
    }
}