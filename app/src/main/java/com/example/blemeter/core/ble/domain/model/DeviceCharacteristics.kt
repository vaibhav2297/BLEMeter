package com.example.blemeter.core.ble.domain.model

data class DeviceCharacteristics(
    val uuid: String,
    val name: String,
    val permissions: Int,
    val descriptor: String?,
    val properties: List<BleProperties>,
    val writeTypes: List<BleWriteTypes>,
    val descriptors: List<DeviceDescriptor>,
    val canRead: Boolean,
    val canWrite: Boolean,
    val readBytes: ByteArray?,
    val notificationBytes: ByteArray?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DeviceCharacteristics

        if (uuid != other.uuid) return false
        if (name != other.name) return false
        if (permissions != other.permissions) return false
        if (properties != other.properties) return false
        if (writeTypes != other.writeTypes) return false
        if (descriptors != other.descriptors) return false
        if (canRead != other.canRead) return false
        if (canWrite != other.canWrite) return false
        if (readBytes != null) {
            if (other.readBytes == null) return false
            if (!readBytes.contentEquals(other.readBytes)) return false
        } else if (other.readBytes != null) return false
        if (notificationBytes != null) {
            if (other.notificationBytes == null) return false
            if (!notificationBytes.contentEquals(other.notificationBytes)) return false
        } else if (other.notificationBytes != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uuid.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + permissions
        result = 31 * result + properties.hashCode()
        result = 31 * result + writeTypes.hashCode()
        result = 31 * result + canRead.hashCode()
        result = 31 * result + canWrite.hashCode()
        result = 31 * result + (readBytes?.contentHashCode() ?: 0)
        result = 31 * result + (notificationBytes?.contentHashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return """
            DeviceCharacteristics:
                UUID: $uuid
                Name: $name
                Permissions: $permissions
                Descriptor: $descriptor
                Properties: $properties
                Write Types: $writeTypes
                Descriptors: $descriptors
                Can Read: $canRead
                Can Write: $canWrite
                Read Bytes: ${readBytes?.contentToString() ?: "null"}
                Notification Bytes: ${notificationBytes?.contentToString() ?: "null"}
        """.trimIndent()
    }
}

fun DeviceCharacteristics.hasNotify(): Boolean = properties.contains(BleProperties.PROPERTY_NOTIFY)

fun DeviceCharacteristics.hasIndicate(): Boolean = properties.contains(BleProperties.PROPERTY_INDICATE)

fun DeviceCharacteristics.updateBytes(fromDevice: ByteArray): DeviceCharacteristics {
    return copy(readBytes = fromDevice)
}

fun DeviceCharacteristics.updateDescriptors(uuidFromDevice: String, fromDevice: ByteArray): List<DeviceDescriptor> {
    return descriptors.map {
        if (it.uuid == uuidFromDevice)
            it.copy(readBytes = fromDevice)
        else
            it
    }
}

fun DeviceCharacteristics.updateNotification(fromDevice: ByteArray): DeviceCharacteristics {
    return copy(readBytes = fromDevice)
}
