package com.example.blemeter.core.ble.domain.model.request

import com.example.blemeter.model.MeterAddress
import com.example.blemeter.model.MeterType

/**
 * Class represents the base request that contains in every command.
 */
data class BaseRequest(
    override val meterType: MeterType = MeterType.Unknown,
    override val meterAddress: MeterAddress = MeterAddress(),
    override val serialNumber: Int = 0
) : Base {
}

interface Base {
    val meterType: MeterType
    val meterAddress: MeterAddress
    val serialNumber: Int
}

fun BaseRequest.toByteArray(): ByteArray {
    val meterTypeBytes = meterType.code.toByte()
    val meterAddressBytes = meterAddress.toByteArray()

    return byteArrayOf(meterTypeBytes, *meterAddressBytes)
}