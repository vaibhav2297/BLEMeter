package com.example.blemeter.core.ble.domain.model

enum class DataIdentifier(val identifier: String) {
    NONE(""),
    METER_DATA("901f"),
    VALVE_CONTROL_DATA("a017"),
    PURCHASE_DATA("a013"),
    ACCUMULATION("a035"),
    ZERO_INITIALISATION("a021");

    companion object {
        fun getDataType(hexCommand: String) : DataIdentifier {
            val identifier = hexCommand.substring(22..25).lowercase()
            return when(identifier) {
                METER_DATA.identifier -> METER_DATA
                VALVE_CONTROL_DATA.identifier -> VALVE_CONTROL_DATA
                PURCHASE_DATA.identifier -> PURCHASE_DATA
                ACCUMULATION.identifier -> ACCUMULATION
                ZERO_INITIALISATION.identifier -> ZERO_INITIALISATION
                else -> NONE
            }
        }
    }
}