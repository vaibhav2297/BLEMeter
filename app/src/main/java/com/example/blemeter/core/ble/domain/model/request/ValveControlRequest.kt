package com.example.blemeter.core.ble.domain.model.request

data class ValveControlRequest(
    val baseRequest: BaseRequest,
    val status: ValveControlCommandStatus
) : Base by baseRequest


enum class ValveControlCommandStatus(val code: Int) {
    NONE(-1),
    OPEN(85), //055H
    CLOSE(153); //99H

    companion object {
        fun getValveControlCommandStatus(code: Int): ValveControlCommandStatus {
            return when(code) {
                OPEN.code -> OPEN
                CLOSE.code -> CLOSE
                else -> NONE
            }
        }
    }
}
