package com.example.blemeter.core.ble.domain.bleparsable

import com.example.blemeter.core.ble.domain.model.DataIdentifier
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.ble.utils.toUInt16
import com.example.blemeter.core.ble.utils.toUInt32
import com.example.blemeter.model.MeterAddress
import com.example.blemeter.model.MeterType
import com.example.blemeter.model.Spare

@ExperimentalUnsignedTypes
object MeterAddressCommand : Command<Spare, MeterAddress>(MeterServicesProvider.DeviceInfo.MODEL_NUMBER) {

    override val controlCode: Int = -1

    override val requestLength: Int = 0

    override val dataIdentifier: DataIdentifier = DataIdentifier.NONE

    override fun toCommand(request: Spare): UByteArray {
        return ubyteArrayOf()
    }

    override fun fromCommand(command: UByteArray): MeterAddress {
        command.run {
            require(this.size > 6) { "Not enough bytes to retrieve meter address" }

            val addressCode = this.toUInt32(0)
            val meterType = MeterType.getMeterType(this[4])
            val manufacturerCode = this.toUInt16(5)

            return MeterAddress(
                addressCode = addressCode.toInt(),
                meterType = meterType,
                manufacturerCode = manufacturerCode.toInt()
            )
        }
    }
}