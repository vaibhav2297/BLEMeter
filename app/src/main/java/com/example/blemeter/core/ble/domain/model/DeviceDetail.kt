package com.example.blemeter.core.ble.domain.model

data class DeviceDetail(
    val device: ScannedDevice?,
    val services: List<DeviceService>,
    val connectionState: ConnectionState,
    val isSupportConfig: Boolean
) {
    override fun toString(): String {
        return """
            DeviceDetail:
                Device: $device
                Services:
                ${services.joinToString("\n") { service ->
            service.toString().prependIndent("    ")
        }}
                Connection State: $connectionState
                Supports Configuration: $isSupportConfig
        """.trimIndent()
    }
}

data class DeviceService(
    val uuid: String,
    val name: String,
    val characteristics: List<DeviceCharacteristics>
) {
    override fun toString(): String {
        return """
            DeviceService:
                UUID: $uuid
                Name: $name
                Characteristics:
                ${characteristics.joinToString("\n") { characteristic ->
            characteristic.toString().prependIndent("    ")
        }}
        """.trimIndent()
    }
}

fun List<DeviceService>.toFormattedString(): String {
    return """
        Services:
        ${joinToString("\n") { service ->
        service.toString().prependIndent("    ")
    }}
    """.trimIndent()
}

fun List<DeviceService>.hasConfigCharacteristic(configUUID: String): Boolean {
    return any { service ->
        service.characteristics.any { char ->
            char.uuid == configUUID
        }
    }
}
