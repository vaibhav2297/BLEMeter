package com.example.blemeter.model

interface ControlState {
    fun code(): Int
}

enum class ValveStatus(private val code: Int) : ControlState {
    OPEN(0), //00 bit
    CLOSE(1), //01 bit
    ABNORMAL(3); // 11 bit

    override fun code(): Int {
        return this.code
    }

    companion object {
        fun getState(byte: UByte): ControlState {
            val stateBits = (byte.toInt() shr 6) and 3 // Get first 2 bits and mask to 0 or 3
            return when (stateBits) {
                0 -> OPEN
                1 -> CLOSE
                3 -> ABNORMAL
                else -> throw IllegalArgumentException("Invalid binary string for Valve state: $stateBits")
            }
        }
    }
}

enum class RelayStatus(private val code: Int) : ControlState {
    CONDUCTING_ELECTRICITY(0),  //00 bit
    DISCONNECT_POWER(1);        //01 bit

    override fun code(): Int {
        return this.code
    }

    companion object {
        fun getState(byte: UByte): ControlState {
            val stateBits = (byte.toInt() shr 6) and 3 // Get first 2 bits and mask to 0 or 3
            return when (stateBits) {
                0 -> CONDUCTING_ELECTRICITY
                1 -> DISCONNECT_POWER
                else -> throw IllegalArgumentException("Invalid binary string for Relay state: $stateBits")
            }
        }
    }
}

enum class NoneStatus(private val code: Int) : ControlState {
    NONE(-1);

    override fun code(): Int = NONE.code
}

fun getControlState(meterType: MeterType, byte: UByte): ControlState {
    return when(meterType) {
        is MeterType.WaterMeter -> ValveStatus.getState(byte)
        is MeterType.ElectricityMeter -> RelayStatus.getState(byte)
        else -> NoneStatus.NONE
    }
}