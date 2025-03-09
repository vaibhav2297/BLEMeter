package com.example.blemeter.core.ble.domain.command

import com.example.blemeter.config.model.MeterConfig
import com.example.blemeter.core.ble.domain.model.DataIdentifier
import com.example.blemeter.config.extenstions.accumulateUByteArray

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

    abstract val controlCode: UByte

    abstract val requestLength: UInt

    abstract val dataIdentifier: DataIdentifier

    abstract val serialNumber: UByte

    /**
     * Accumulate the commands value and return single [UByte]
     */
    fun checkCode(commands: UByteArray) : UByte =
        commands.accumulateUByteArray()

    /**
     * converts the settings into command
     *
     */
    abstract fun toCommand(request: T, meterConfig: MeterConfig): UByteArray

    /**
     * converts response received from BLE into its respective setting class
     *
     */
    abstract fun fromCommand(command: String): R
}