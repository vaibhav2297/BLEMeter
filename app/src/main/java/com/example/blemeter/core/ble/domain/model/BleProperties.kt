package com.example.blemeter.core.ble.domain.model

enum class BleProperties(val value: Int) {
    PROPERTY_BROADCAST(1),
    PROPERTY_EXTENDED_PROPS(128),
    PROPERTY_INDICATE(32),
    PROPERTY_NOTIFY(16),
    PROPERTY_READ(2),
    PROPERTY_SIGNED_WRITE(64),
    PROPERTY_WRITE(8),
    PROPERTY_WRITE_NO_RESPONSE(4);

    companion object {

        fun getAllProperties(bleValue: Int): List<BleProperties> {
            val propertyList = mutableListOf<BleProperties>()

            entries.forEach {
                if (bleValue and it.value > 0)
                    propertyList.add(it)
            }
            return propertyList
        }

        fun List<BleProperties>.canRead(): Boolean = this.contains(PROPERTY_READ)

        fun List<BleProperties>.canWriteProperties(): Boolean = this.any(
            listOf(
                PROPERTY_WRITE, PROPERTY_SIGNED_WRITE,
                PROPERTY_WRITE_NO_RESPONSE
            )::contains
        )
    }
}