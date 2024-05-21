package com.example.blemeter.core.ble.domain.model

data class DeviceDescriptor(
    val uuid: String,
    val name: String,
    val charUuid: String,
    val permissions: List<BlePermissions>,
    val notificationProperty: BleProperties?,
    val readBytes: ByteArray?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DeviceDescriptor

        if (uuid != other.uuid) return false
        if (name != other.name) return false
        if (charUuid != other.charUuid) return false
        if (permissions != other.permissions) return false
        if (notificationProperty != other.notificationProperty) return false
        if (readBytes != null) {
            if (other.readBytes == null) return false
            if (!readBytes.contentEquals(other.readBytes)) return false
        } else if (other.readBytes != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uuid.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + charUuid.hashCode()
        result = 31 * result + permissions.hashCode()
        result = 31 * result + (notificationProperty?.hashCode() ?: 0)
        result = 31 * result + (readBytes?.contentHashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return """
            DeviceDescriptor:
                UUID: $uuid
                Name: $name
                Characteristic UUID: $charUuid
                Permissions: $permissions
                Notification Property: $notificationProperty
                Read Bytes: ${readBytes?.contentToString() ?: "null"}
        """.trimIndent()
    }
}



