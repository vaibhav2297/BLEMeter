package com.example.blemeter.core.ble.domain.bleparsable

import com.example.blemeter.core.ble.domain.model.DataIdentifier
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.ble.domain.model.request.MeterDataRequest
import com.example.blemeter.core.ble.utils.BLEConstants
import com.example.blemeter.core.ble.utils.fromHexToUByteArray
import com.example.blemeter.core.ble.utils.toUInt32
import com.example.blemeter.model.BatteryVoltage
import com.example.blemeter.model.MeterData
import com.example.blemeter.model.MeterType
import com.example.blemeter.model.Statuses
import com.example.blemeter.model.getControlState

@ExperimentalUnsignedTypes
object MeterDataCommand :
    Command<MeterDataRequest, MeterData>(MeterServicesProvider.MainService.WRITE_CHARACTERISTIC) {

    override val controlCode: Int = 1

    override val requestLength: Int = 3

    override val dataIdentifier: DataIdentifier = DataIdentifier.METER_DATA

    override fun toCommand(request: MeterDataRequest): UByteArray {

        //uByte array to hold bytes before check code
        // for accumulate total byte
        val arr = ubyteArrayOf(
            BLEConstants.SOF,
            MeterType.WaterMeter.ColdWaterMeter.code.toUByte(),
            *BLEConstants.METER_ADDRESS.fromHexToUByteArray(),
            controlCode.toUByte(),
            requestLength.toUByte(),
//            144u.toUByte(),         //90H - data identification low byte
//            31u.toUByte(),          //1FH - data identification high byte
            *getDataIdentifierByteArray(),
            0u.toUByte()            //00F - serial number
        )

        return ubyteArrayOf(
            *arr,
            checkCode(arr), // accumulate total bytes
            BLEConstants.EOF
        )
    }

    @Throws
    override fun fromCommand(command: UByteArray): MeterData {

        require(command.size > 39) { "Expected Meter Data response size should be of 40 bytes. but it is ${command.size}" }

        command.run {
            val meterType = MeterType.getMeterType(this[1])
            val statuses = Statuses(
                batteryState = BatteryVoltage.getBatteryVoltage(this[27]),
                controlState = getControlState(meterType, this[27])
            )

            return MeterData(
                accumulatedUsage = this.toUInt32(14).toInt(),
                surplus = this.toUInt32(18).toInt(),
                totalPurchase = this.toUInt32(22).toInt(),
                numberTimes = this[26].toInt(),
                statuses = statuses,
                alarmVariable = this[29].toInt(),
                overdraft = this[30].toInt(),
                minimumUsage = this[31].toInt(),
                additionDeduction = this[32].toInt(),
                productVersion = this[33].toInt(),
                programVersion = this[34].toInt()
            )
        }
    }
}