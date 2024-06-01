package com.example.blemeter.core.ble.domain.bleparsable

import com.example.blemeter.core.ble.domain.model.DataIdentifier
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.ble.domain.model.request.ValveControlRequest
import com.example.blemeter.core.ble.utils.BLEConstants
import com.example.blemeter.core.ble.utils.fromHexToByteArray
import com.example.blemeter.core.ble.utils.toHighByte
import com.example.blemeter.model.MeterType
import com.example.blemeter.model.ValveControlData

@ExperimentalUnsignedTypes
object ValveControlCommand :
    Command<ValveControlRequest, ValveControlData>(
        serviceUuid = MeterServicesProvider.MainService.SERVICE,
        characteristicUuid = MeterServicesProvider.MainService.WRITE_CHARACTERISTIC
    ) {

    override val controlCode: Int = 4

    override val requestLength: Int = 7

    override val dataIdentifier: DataIdentifier = DataIdentifier.VALVE_CONTROL_DATA

    override fun toCommand(request: ValveControlRequest): ByteArray {

        //uByte array to hold bytes before check code
        // for accumulate total byte
        val arr = byteArrayOf(
            BLEConstants.SOF,
            MeterType.WaterMeter.ColdWaterMeter.code.toByte(),
            *BLEConstants.METER_ADDRESS.fromHexToByteArray(),
            controlCode.toByte(),
            requestLength.toByte(),
//            160.toUByte(),                          //90H - data identification low byte
//            23.toUByte(),                           //1FH - data identification high byte
            *getDataIdentifierByteArray(),
            0u.toByte(),                            //00F - serial number
            request.status.code.toByte(),          // Open 055H  Close 99H
            BLEConstants.STAND_BY,
            BLEConstants.STAND_BY,
            BLEConstants.STAND_BY
        )

        return byteArrayOf(
            *arr,
            checkCode(arr).toByte(), // accumulate total bytes
            BLEConstants.EOF
        )
    }

    override fun fromCommand(command: ByteArray): ValveControlData {
        require(command.size > 18) { "Expected Valve Control Data response size should be of 19 bytes. but it is ${command.size}" }

        command.run {
            return ValveControlData()
        }
    }
}