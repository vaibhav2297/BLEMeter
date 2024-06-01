package com.example.blemeter.model

import com.example.blemeter.core.ble.utils.getBits

interface ControlState {
    fun code(): Int
}

enum class ValveStatus(private val code: Int) : ControlState {
    OPEN(0b00), //00 bit
    CLOSE(0b01), //01 bit
    ABNORMAL(0b11); // 11 bit

    override fun code(): Int {
        return this.code
    }

    companion object {
        fun getState(byte: Byte): ControlState {
            //val stateBits = (byte.toInt() shr 6) and 3 // Get first 2 bits and mask to 0 or 3
            return when (val stateBits = byte.getBits(0,1)) {
                OPEN.code -> OPEN
                CLOSE.code -> CLOSE
                ABNORMAL.code -> ABNORMAL
                else -> throw IllegalArgumentException("Invalid binary string for Valve state: $stateBits")
            }
        }
    }
}

enum class RelayStatus(private val code: Int) : ControlState {
    CONDUCTING_ELECTRICITY(0b00),  //00 bit
    DISCONNECT_POWER(0b01);        //01 bit

    override fun code(): Int {
        return this.code
    }

    companion object {
        fun getState(byte: Byte): ControlState {
            //val stateBits = (byte.toInt() shr 6) and 3 // Get first 2 bits and mask to 0 or 3
            return when (val stateBits = byte.getBits(0,1)) {
                CONDUCTING_ELECTRICITY.code -> CONDUCTING_ELECTRICITY
                DISCONNECT_POWER.code -> DISCONNECT_POWER
                else -> throw IllegalArgumentException("Invalid binary string for Relay state: $stateBits")
            }
        }
    }
}

enum class NoneStatus(private val code: Int) : ControlState {
    NONE(-1);

    override fun code(): Int = NONE.code
}

fun getControlState(meterType: MeterType, byte: Byte): ControlState {
    return when(meterType) {
        is MeterType.WaterMeter -> ValveStatus.getState(byte)
        is MeterType.ElectricityMeter -> RelayStatus.getState(byte)
        else -> NoneStatus.NONE
    }
}