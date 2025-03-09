package com.example.blemeter.core.ble.domain.command

import com.example.blemeter.config.model.MeterConfig
import com.example.blemeter.core.ble.domain.model.DataIdentifier
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.config.constants.BLEConstants
import com.example.blemeter.config.extenstions.fromHexToUByteArray
import com.example.blemeter.config.model.CalibrationIdentification
import com.example.blemeter.config.model.InPlaceMethod
import com.example.blemeter.config.model.NumberingInstructionData
import com.example.blemeter.core.ble.domain.model.request.NumberingInstructionDataRequest

@OptIn(ExperimentalUnsignedTypes::class)
object NumberingInstructionDataCommand :
    Command<NumberingInstructionDataRequest, NumberingInstructionData>(
        serviceUuid = MeterServicesProvider.MainService.SERVICE,
        characteristicUuid = MeterServicesProvider.MainService.WRITE_CHARACTERISTIC
    ) {

    override val controlCode: UByte = 0x04u

    override val requestLength: UInt = 0x1bu

    override val dataIdentifier: DataIdentifier = DataIdentifier.NUMBERING_INSTRUCTION

    override val serialNumber: UByte = 0x00u

    override fun toCommand(
        request: NumberingInstructionDataRequest,
        meterConfig: MeterConfig
    ): UByteArray {

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
            *meterConfig.meterAddress.fromHexToUByteArray(),
            request.calibrationIdentification.commandBit.toUByte(),
            request.inPlaceMethod.commandBit.toUByte(),
            request.paymentMethod.commandBit.toUByte(),
            *UByteArray(14) { BLEConstants.STAND_BY }
        )

        return ubyteArrayOf(
            *arr,
            checkCode(arr), // accumulate total bytes
            BLEConstants.EOF
        )
    }

    @Throws
    override fun fromCommand(command: String): NumberingInstructionData {

        require(command.length > 39 * 2) { "Expected Numbering instruction response length should be of 78. but it is ${command.length}" }

        command.run {
            val calibrationIdentification =
                CalibrationIdentification.getCalibrationIdentificationByCommandBit(substring(42..43).toUInt(16))
            val inPlaceMethod = InPlaceMethod.getInPlaceMethodByCommandBit(substring(46..47).toUInt(16))

            return NumberingInstructionData(
                calibrationIdentification = calibrationIdentification,
                inPlaceMethod = inPlaceMethod,
            )
        }
    }
}