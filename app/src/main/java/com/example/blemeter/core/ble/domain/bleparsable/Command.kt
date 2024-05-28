package com.example.blemeter.core.ble.domain.bleparsable

import com.example.blemeter.core.ble.domain.model.DataIdentifier
import com.example.blemeter.core.ble.utils.toHighUByte
import com.example.blemeter.core.ble.utils.toLowUByte

@OptIn(ExperimentalUnsignedTypes::class)

/**
 * An abstract base class for all settings-related classes.
 * @param T The type of setting object that should converted into command
 * @param R The type of setting object that should converted from command
 */
abstract class Command<T, R>(val uuid: String) {

    abstract val controlCode: Int

    abstract val requestLength: Int

    abstract val dataIdentifier: DataIdentifier

    /**
     * Accumulate the commands value and return single [UByte]
     */
    fun checkCode(commands: UByteArray) =
        commands.sum().toUByte()

    /**
     * @return [UByteArray] of the data identifier
     */
    fun getDataIdentifierByteArray() =
        ubyteArrayOf(
            dataIdentifier.identifier.toHighUByte(),
            dataIdentifier.identifier.toLowUByte()
        )

    /**
     * converts the settings into command
     *
     */
    abstract fun toCommand(request: T): UByteArray

    /**
     * converts response received from BLE into its respective setting class
     *
     */
    abstract fun fromCommand(command: UByteArray): R
}