package com.example.blemeter.core.ble.domain.bleparsable

import com.example.blemeter.core.ble.domain.model.DataIdentifier
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.ble.utils.toUInt16
import com.example.blemeter.core.ble.utils.toUInt32
import com.example.blemeter.model.MeterAddress
import com.example.blemeter.model.MeterType
import com.example.blemeter.model.Spare

@ExperimentalUnsignedTypes
object SerialNumberCommand : Command<Spare, Int>(MeterServicesProvider.DeviceInfo.MODEL_NUMBER) {

    override val controlCode: Int = -1

    override val requestLength: Int = 0

    override val dataIdentifier: DataIdentifier = DataIdentifier.NONE

    override fun toCommand(request: Spare): UByteArray {
        return ubyteArrayOf()
    }

    override fun fromCommand(command: UByteArray): Int {
        command.run {
            require(this.isNotEmpty()) { "Not enough bytes to retrieve serial number" }

            return this[0].toInt()
        }
    }
}