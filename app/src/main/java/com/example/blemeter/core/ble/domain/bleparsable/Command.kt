package com.example.blemeter.core.ble.domain.bleparsable

import com.example.blemeter.core.ble.domain.model.DataIdentifier
import com.example.blemeter.core.ble.utils.accumulateSum
import com.example.blemeter.core.ble.utils.toHighByte
import com.example.blemeter.core.ble.utils.toLowByte

@OptIn(ExperimentalUnsignedTypes::class)

/**
 * An abstract base class for all settings-related classes.
 * @param T The type of setting object that should converted into command
 * @param R The type of setting object that should converted from command
 */
abstract class Command<T, R>(
    val serviceUuid: String,
    val characteristicUuid: String
) {

    abstract val controlCode: Int

    abstract val requestLength: Int

    abstract val dataIdentifier: DataIdentifier

    /**
     * Accumulate the commands value and return single [UByte]
     */
    fun checkCode(commands: ByteArray) : Byte =
        commands.accumulateSum(0, commands.size - 1)

    /**
     * @return [UByteArray] of the data identifier
     */
    fun getDataIdentifierByteArray() =
        byteArrayOf(
            dataIdentifier.identifier.toHighByte(),
            dataIdentifier.identifier.toLowByte()
        )

    /**
     * converts the settings into command
     *
     */
    abstract fun toCommand(request: T): ByteArray

    /**
     * converts response received from BLE into its respective setting class
     *
     */
    abstract fun fromCommand(command: ByteArray): R
}