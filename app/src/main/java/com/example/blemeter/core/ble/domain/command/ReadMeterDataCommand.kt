package com.example.blemeter.core.ble.domain.command

import com.example.blemeter.config.model.MeterConfig
import com.example.blemeter.core.ble.domain.model.DataIdentifier
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.ble.domain.model.request.MeterDataRequest
import com.example.blemeter.config.constants.BLEConstants
import com.example.blemeter.config.extenstions.fromHexToUByteArray
import com.example.blemeter.config.model.BatteryVoltage
import com.example.blemeter.config.model.MeterData
import com.example.blemeter.config.model.MeterType
import com.example.blemeter.config.model.ProductVersion
import com.example.blemeter.config.model.Statuses
import com.example.blemeter.config.model.getControlState

@OptIn(ExperimentalUnsignedTypes::class)
object ReadMeterDataCommand : Command<MeterDataRequest, MeterData>(
    serviceUuid = MeterServicesProvider.MainService.SERVICE,
    characteristicUuid = MeterServicesProvider.MainService.WRITE_CHARACTERISTIC
) {

    override val controlCode: UByte = 0x01u

    override val requestLength: UInt = 0x03u

    override val dataIdentifier: DataIdentifier = DataIdentifier.METER_DATA

    override val serialNumber: UByte = 0x00u

    override fun toCommand(request: MeterDataRequest, meterConfig: MeterConfig): UByteArray {

        //uByte array to hold bytes before check code
        // for accumulate total byte
        val arr = ubyteArrayOf(
            BLEConstants.SOF,
            meterConfig.meterType.code.toUByte(),
            *meterConfig.meterAddress.fromHexToUByteArray(),
            controlCode,
            requestLength.toUByte(),
            *dataIdentifier.identifier.fromHexToUByteArray(),
            serialNumber
        )

        return ubyteArrayOf(
            *arr,
            checkCode(arr), // accumulate total bytes
            BLEConstants.EOF
        )
    }

    @Throws
    override fun fromCommand(command: String): MeterData {

        require(command.length > 39 * 2) { "Expected Meter Data response length should be of 78. but it is ${command.length}" }

        command.run {

            val statusByte = substring(54..55).toUByte(16)
            val meterType = MeterType.getMeterType(substring(2..3).toUInt(16))
            val statuses = Statuses(
                batteryState = BatteryVoltage.getBatteryVoltage(statusByte),
                controlState = getControlState(meterType, statusByte)
            )

            //Product Version
            val productVersionResponse = substring(66..67).toUInt(16)
            val productVersion = ProductVersion.getProductVersionFromResponseBit(productVersionResponse)

            //multiplying factor based on the meter type
            val factor = productVersion.calibrationIdentification.factor.toInt()

            return MeterData(
                accumulatedUsage = (substring(28..35).toLong(16)).times(factor).div(BLEConstants.ONE_METER_CUBE),
                surplus = (substring(36..43).toLong(16)).times(factor).div(BLEConstants.ONE_METER_CUBE),
                totalPurchase = (substring(44..51).toLong(16)).times(factor).div(BLEConstants.ONE_METER_CUBE),
                numberTimes = substring(52..53).toUInt(16),
                statuses = statuses,
                alarmVariable = substring(58..59).toUInt(16),
                overdraft = substring(60..61).toUInt(16),
                minimumUsage = substring(62..63).toUInt(16),
                additionDeduction = substring(64..65).toUInt(16),
                productVersion = productVersion,
                programVersion = substring(68..69).toUInt(16),
            )
        }
    }
}