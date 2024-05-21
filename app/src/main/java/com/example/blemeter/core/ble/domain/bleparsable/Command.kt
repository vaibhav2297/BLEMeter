package com.example.blemeter.core.ble.domain.bleparsable

@OptIn(ExperimentalUnsignedTypes::class)

/**
 * An abstract base class for all settings-related classes.
 * @param T The type of setting object that should converted into command
 * @param R The type of setting object that should converted from command
 */
abstract class Command<T, R>(val uuid: String) {

    abstract val controlCode: Int

    abstract val requestLength: Int

    fun checkCode(commands: UByteArray) =
        commands.sum().toUByte()

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