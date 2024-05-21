package com.example.blemeter.core.ble.domain.usecases

import android.bluetooth.BluetoothGatt
import android.util.Log
import com.example.blemeter.core.ble.data.BLEGATTService
import com.example.blemeter.core.ble.domain.model.BlePermissions
import com.example.blemeter.core.ble.domain.model.BleProperties
import com.example.blemeter.core.ble.domain.model.BleProperties.Companion.canRead
import com.example.blemeter.core.ble.domain.model.BleProperties.Companion.canWriteProperties
import com.example.blemeter.core.ble.domain.model.BleWriteTypes
import com.example.blemeter.core.ble.domain.model.DeviceCharacteristics
import com.example.blemeter.core.ble.domain.model.DeviceDescriptor
import com.example.blemeter.core.ble.domain.model.DeviceService

class ParseService {

    operator fun invoke(
        gatt: BluetoothGatt,
        status: Int
    ): List<DeviceService> {
        val services = mutableListOf<DeviceService>()

        if (status == BluetoothGatt.GATT_SUCCESS) {
            /*Service*/
            gatt.services?.forEach { gattService ->

                //val serviceName = getServiceName(gattService.uuid.toString())

                /*Characteristic*/
                val characteristic = mutableListOf<DeviceCharacteristics>()
                gattService.characteristics.forEach { gattChar ->

                    val permissions = gattChar.permissions
                    val properties = BleProperties.getAllProperties(gattChar.properties)
                    val writeTypes = BleWriteTypes.getAllTypes(gattChar.writeType)
                    //val charName = getCharacteristicName(gattChar.uuid.toString())

                    /*Descriptor*/
                    val descriptors = mutableListOf<DeviceDescriptor>()
                    gattChar.descriptors.forEach { desc ->

                        Log.d(BLEGATTService.TAG, "parseService :: characteristic :${gattChar.uuid}")
                        Log.d(BLEGATTService.TAG, "parseService :: descriptor :${desc.uuid}")

                        descriptors.add(
                            DeviceDescriptor(
                                uuid = desc.uuid.toString(),
                                //name = getDescriptorName(desc.uuid.toString()),
                                name = "",
                                charUuid = desc.characteristic.uuid.toString(),
                                permissions = BlePermissions.getAllPermissions(desc.permissions),
                                notificationProperty = if (properties.contains(BleProperties.PROPERTY_NOTIFY))
                                    BleProperties.PROPERTY_NOTIFY else if (properties.contains(
                                        BleProperties.PROPERTY_INDICATE
                                    )
                                )
                                    BleProperties.PROPERTY_INDICATE else null,
                                readBytes = null
                            )
                        )
                    }

                    characteristic.add(
                        DeviceCharacteristics(
                            uuid = gattChar.uuid.toString(),
                            name = "",
                            descriptor = null,
                            permissions = permissions,
                            writeTypes = writeTypes,
                            properties = properties,
                            canRead = properties.canRead(),
                            canWrite = properties.canWriteProperties(),
                            readBytes = null,
                            notificationBytes = null,
                            descriptors = descriptors
                        )
                    )

                }

                services.add(
                    DeviceService(
                        uuid = gattService.uuid.toString(),
                        name = "",
                        characteristics = characteristic
                    )
                )

                Log.d(BLEGATTService.TAG, "parseService :: services :${services}")
            }
        }

        return services.toList()
    }
}