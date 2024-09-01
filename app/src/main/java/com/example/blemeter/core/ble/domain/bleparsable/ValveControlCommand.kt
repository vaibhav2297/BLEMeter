package com.example.blemeter.core.ble.domain.bleparsable

import com.example.blemeter.config.extenstions.chunkAndReverseString
import com.example.blemeter.config.model.MeterConfig
import com.example.blemeter.core.ble.domain.model.DataIdentifier
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.ble.domain.model.request.ValveControlRequest
import com.example.blemeter.core.ble.utils.BLEConstants
import com.example.blemeter.core.ble.utils.fromHexToUByteArray
import com.example.blemeter.model.MeterType
import com.example.blemeter.model.ValveControlData
import com.example.blemeter.model.getControlState

@ExperimentalUnsignedTypes
object ValveControlCommand :
    Command<ValveControlRequest, ValveControlData>(
        serviceUuid = MeterServicesProvider.MainService.SERVICE,
        characteristicUuid = MeterServicesProvider.MainService.WRITE_CHARACTERISTIC
    ) {

    override val controlCode: UByte = 0x04u

    override val requestLength: UInt = 0x07u

    override val dataIdentifier: DataIdentifier = DataIdentifier.VALVE_CONTROL_DATA

    override val serialNumber: UByte = 0x00u

    override fun toCommand(request: ValveControlRequest, meterConfig: MeterConfig): UByteArray {

        //uByte array to hold bytes before check code
        // for accumulate total byte
        val arr = ubyteArrayOf(
            BLEConstants.SOF,
            meterConfig.meterType.code.toUByte(),
            *meterConfig.meterAddress.chunkAndReverseString().fromHexToUByteArray(),
            controlCode,
            requestLength.toUByte(),
            *dataIdentifier.identifier.fromHexToUByteArray(),
            serialNumber,
            request.status.code,
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

    /**
     * E.x
     * 68 -> SOF
     * 12 -> Meter Type
     * 02 00 18 03 24 96 71 -> Meter Address
     * 84 -> Control Code
     * 07 -> Length
     * A0 17 -> Data Identification
     * 00 -> Serial Number
     * 00 00 -> Status Code
     * 00 00 -> Stand by
     * 02 -> Check Sum
     * 16 -> EOF
     */
    override fun fromCommand(command: String): ValveControlData {
        require(command.length > 18 * 2) { "Expected Valve Control Data response length should be of 38. but it is ${command.length}" }

        command.run {

            val meterType = MeterType.getMeterType(BLEConstants.METER_TYPE)
            val statusByte = substring(28..29).toUByte(16)
            val valveStatus = getControlState(meterType, statusByte)

            return ValveControlData(controlState = valveStatus)
        }
    }
}