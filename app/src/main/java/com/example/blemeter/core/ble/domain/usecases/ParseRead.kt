package com.example.blemeter.core.ble.domain.usecases

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import com.example.blemeter.core.ble.domain.model.DeviceService
import com.example.blemeter.core.ble.domain.model.updateBytes

class ParseRead {

    operator fun invoke(
        deviceDetail: List<DeviceService>,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ): List<DeviceService> {
        return invoke(
            deviceDetail = deviceDetail,
            characteristic = characteristic,
            value = characteristic.value,
            status = status
        )
    }

    operator fun invoke(
        deviceDetail: List<DeviceService>,
        characteristic: BluetoothGattCharacteristic,
        value: ByteArray,
        status: Int
    ): List<DeviceService> {

        if (status == BluetoothGatt.GATT_SUCCESS) {
            val newList = deviceDetail.map { svc ->
                svc.copy(
                    characteristics = svc.characteristics.map { char ->
                        if (char.uuid == characteristic.uuid.toString()) {
                            char.updateBytes(value)
                        } else
                            char
                    }
                )
            }

            return newList
        } else {
            return deviceDetail
        }
    }
}