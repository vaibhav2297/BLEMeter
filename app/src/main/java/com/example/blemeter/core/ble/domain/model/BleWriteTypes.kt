package com.example.blemeter.core.ble.domain.model

enum class BleWriteTypes(val value: Int) {
    WRITE_TYPE_DEFAULT(2),
    WRITE_TYPE_NO_RESPONSE(1),
    WRITE_TYPE_SIGNED(4);

    companion object {

        fun getAllTypes(bleValue: Int): List<BleWriteTypes> {
            val propertyList = mutableListOf<BleWriteTypes>()

            entries.forEach {
                if (bleValue and it.value > 0)
                    propertyList.add(it)
            }
            return propertyList

        }
    }
}