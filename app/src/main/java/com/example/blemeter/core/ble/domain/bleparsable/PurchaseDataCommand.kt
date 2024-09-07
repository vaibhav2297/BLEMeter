package com.example.blemeter.core.ble.domain.bleparsable

import com.example.blemeter.config.extenstions.chunkAndReverseString
import com.example.blemeter.config.model.MeterConfig
import com.example.blemeter.core.ble.domain.model.DataIdentifier
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.ble.domain.model.request.PurchaseDataRequest
import com.example.blemeter.core.ble.utils.BLEConstants
import com.example.blemeter.core.ble.utils.fromHexToUByteArray
import com.example.blemeter.core.ble.utils.to4UByteArray
import com.example.blemeter.model.BatteryVoltage
import com.example.blemeter.model.MeterData
import com.example.blemeter.model.MeterType
import com.example.blemeter.model.Statuses
import com.example.blemeter.model.getControlState

@OptIn(ExperimentalUnsignedTypes::class)
object PurchaseDataCommand : Command<PurchaseDataRequest, MeterData>(
    serviceUuid = MeterServicesProvider.MainService.SERVICE,
    characteristicUuid = MeterServicesProvider.MainService.WRITE_CHARACTERISTIC
) {

    override val controlCode: UByte = 0x04u

    override val requestLength: UInt = 0x08u

    override val dataIdentifier: DataIdentifier = DataIdentifier.PURCHASE_DATA

    override val serialNumber: UByte = 0x00u

    override fun toCommand(request: PurchaseDataRequest, meterConfig: MeterConfig): UByteArray {

        //uByte array to hold bytes before check code
        // for accumulate total byte
        val arr = ubyteArrayOf(
            BLEConstants.SOF,
            meterConfig.meterType.code.toUByte(),
            *meterConfig.meterAddress.fromHexToUByteArray(),
            controlCode,
            requestLength.toUByte(),
            *dataIdentifier.identifier.fromHexToUByteArray(),
            serialNumber,
            request.numberTimes.toUByte(),
            *(request.purchaseVariable * 100.0).toUInt().to4UByteArray()
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
            val meterType = MeterType.getMeterType(BLEConstants.METER_TYPE)
            val statuses = Statuses(
                batteryState = BatteryVoltage.getBatteryVoltage(statusByte),
                controlState = getControlState(meterType, statusByte)
            )
            return MeterData(
                accumulatedUsage = (substring(28..35).toInt(16)) / 100.0,
                surplus = (substring(36..43).toInt(16)) / 100.0,
                totalPurchase = (substring(44..51).toInt(16)) / 100.0,
                numberTimes = substring(52..53).toUInt(),
                statuses = statuses,
                alarmVariable = substring(58..59).toUInt(),
                overdraft = substring(60..61).toUInt(),
                minimumUsage = substring(62..63).toUInt(),
                additionDeduction = substring(64..65).toUInt(),
                productVersion = substring(66..67).toUInt(),
                programVersion = substring(68..69).toUInt(),
            )
        }
    }
}