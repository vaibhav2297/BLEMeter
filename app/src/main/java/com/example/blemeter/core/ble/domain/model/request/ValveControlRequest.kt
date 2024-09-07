package com.example.blemeter.core.ble.domain.model.request

import com.example.blemeter.model.ValveStatus

data class ValveControlRequest(
    val status: ValveInteractionCommand
)


enum class ValveInteractionCommand(val code: UByte) {

    NONE(255u),
    OPEN(0x55u),
    CLOSE(0x99u);

    companion object {
        fun getValveControlCommandStatus(code: UByte): ValveInteractionCommand {
            return when(code) {
                OPEN.code -> OPEN
                CLOSE.code -> CLOSE
                else -> NONE
            }
        }

        fun ValveInteractionCommand.toValveStatus(): ValveStatus {
            return when(this) {
                OPEN -> ValveStatus.OPEN
                CLOSE -> ValveStatus.CLOSE
                NONE -> ValveStatus.NONE
            }
        }
    }
}
