package com.example.blemeter.core.ble.domain.usecases

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattDescriptor
import com.example.blemeter.core.ble.domain.model.DeviceService
import com.example.blemeter.core.ble.domain.model.updateDescriptors

@Suppress("DEPRECATION")
class ParseDescriptor {

    operator fun invoke(
        deviceDetails: List<DeviceService>,
        descriptor: BluetoothGattDescriptor,
        status: Int
    ): List<DeviceService> {
        return invoke(
            deviceDetails = deviceDetails,
            descriptor = descriptor,
            status = status,
            value = descriptor.value
        )
    }

    operator fun invoke(
        deviceDetails: List<DeviceService>,
        descriptor: BluetoothGattDescriptor,
        status: Int,
        value: ByteArray
    ): List<DeviceService> {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            val newList = deviceDetails.map { dd ->
                dd.copy(characteristics =
                dd.characteristics.map { char ->
                    if (descriptor.characteristic.uuid.toString() == char.uuid) {
                        char.copy(
                            descriptors =
                            char.updateDescriptors(descriptor.uuid.toString(), value)
                        )
                    } else
                        char
                })
            }
            return newList
        } else {
            return deviceDetails
        }
    }
}
