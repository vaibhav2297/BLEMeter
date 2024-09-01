package com.example.blemeter.core.ble.domain.model.request

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
    }
}
