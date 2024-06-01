package com.example.blemeter.core.ble.domain.bleparsable

import com.example.blemeter.core.ble.domain.model.DataIdentifier
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.ble.domain.model.request.MeterDataRequest
import com.example.blemeter.core.ble.domain.model.request.toByteArray
import com.example.blemeter.core.ble.utils.BLEConstants
import com.example.blemeter.core.ble.utils.toHighByte
import com.example.blemeter.core.ble.utils.toInt32
import com.example.blemeter.model.BatteryVoltage
import com.example.blemeter.model.MeterData
import com.example.blemeter.model.MeterType
import com.example.blemeter.model.Statuses
import com.example.blemeter.model.getControlState

object MeterDataCommand :
    Command<MeterDataRequest, MeterData>(
        serviceUuid = MeterServicesProvider.MainService.SERVICE,
        characteristicUuid = MeterServicesProvider.MainService.WRITE_CHARACTERISTIC
    ) {

    override val controlCode: Int = 1

    override val requestLength: Int = 3

    override val dataIdentifier: DataIdentifier = DataIdentifier.METER_DATA

    override fun toCommand(request: MeterDataRequest): ByteArray {

        //uByte array to hold bytes before check code
        // for accumulate total byte
        val arr = byteArrayOf(
            BLEConstants.SOF,
            *request.baseRequest.toByteArray(),
            controlCode.toByte(),
            requestLength.toByte(),
            *getDataIdentifierByteArray(),
            0 //00F - serial number
        )

        return byteArrayOf(
            *arr,
            checkCode(arr), // accumulate total bytes
            BLEConstants.EOF
        )
    }

    @Throws
    override fun fromCommand(command: ByteArray): MeterData {

        require(command.size > 39) { "Expected Meter Data response size should be of 40 bytes. but it is ${command.size}" }

        command.run {
            val meterType = MeterType.getMeterType(this[1])
            val statuses = Statuses(
                batteryState = BatteryVoltage.getBatteryVoltage(this[27]),
                controlState = getControlState(meterType, this[27])
            )

            return MeterData(
                accumulatedUsage = this.toInt32(14),
                surplus = this.toInt32(18),
                totalPurchase = this.toInt32(22),
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