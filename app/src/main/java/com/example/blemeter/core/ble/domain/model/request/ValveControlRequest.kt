package com.example.blemeter.core.ble.domain.model.request

data class ValveControlRequest(
    val status: ValveControlCommandStatus,
    val baseRequest: BaseRequest = BaseRequest()
) : Base by baseRequest


enum class ValveControlCommandStatus(val code: UByte) {

    NONE(255u),
    OPEN(0x55u),
    CLOSE(0x99u);

    companion object {
        fun getValveControlCommandStatus(code: UByte): ValveControlCommandStatus {
            return when(code) {
                OPEN.code -> OPEN
                CLOSE.code -> CLOSE
                else -> NONE
            }
        }
    }
}
