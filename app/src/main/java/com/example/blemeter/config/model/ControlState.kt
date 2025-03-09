package com.example.blemeter.config.model

import com.example.blemeter.core.ble.domain.model.request.ValveInteractionCommand
import com.example.blemeter.config.extenstions.getBits

interface ControlState {
    fun code(): UInt
    fun title(): String
}

enum class ValveStatus(private val code: UInt) : ControlState {
    NONE(0xFFFFFFFFu),
    OPEN(0b00u), //00 bit
    CLOSE(0b10u), //01 bit
    ABNORMAL(0b11u); // 11 bit

    override fun code(): UInt {
        return this.code
    }

    override fun title(): String {
        return this.name
    }

    companion object {
        fun getState(byte: UByte): ControlState {
            return when (byte.getBits(0,1)) {
                OPEN.code -> OPEN
                CLOSE.code -> CLOSE
                ABNORMAL.code -> ABNORMAL
                else -> NONE
            }
        }

        fun ValveStatus.toValveInteraction(): ValveInteractionCommand {
            return when(this) {
                OPEN -> ValveInteractionCommand.OPEN
                CLOSE -> ValveInteractionCommand.CLOSE
                else -> ValveInteractionCommand.NONE
            }
        }
    }
}

enum class RelayStatus(private val code: UInt) : ControlState {
    NONE(0xFFFFFFFFu),
    CONDUCTING_ELECTRICITY(0b00u),  //00 bit
    DISCONNECT_POWER(0b01u);        //01 bit

    override fun code(): UInt {
        return this.code
    }

    override fun title(): String {
        return this.name
    }

    companion object {
        fun getState(byte: UByte): ControlState {
            return when (byte.getBits(0,1)) {
                CONDUCTING_ELECTRICITY.code -> CONDUCTING_ELECTRICITY
                DISCONNECT_POWER.code -> DISCONNECT_POWER
                else -> NONE
            }
        }
    }
}

enum class NoneStatus(private val code: UInt) : ControlState {
    NONE(0xFFFFFFFFu);

    override fun title(): String {
        return this.name
    }

    override fun code(): UInt = NONE.code
}

fun getControlState(meterType: MeterType, byte: UByte): ControlState {
    return when(meterType) {
        is MeterType.WaterMeter -> ValveStatus.getState(byte)
        is MeterType.ElectricityMeter -> RelayStatus.getState(byte)
        else -> NoneStatus.NONE
    }
}