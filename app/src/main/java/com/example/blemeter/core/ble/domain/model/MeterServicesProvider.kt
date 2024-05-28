package com.example.blemeter.core.ble.domain.model

import java.util.UUID

object MeterServicesProvider {

    object GenericAccess {
        const val SERVICE = "00001800-0000-1000-8000-00805f9b34fb"

        const val DEVICE_NAME_CHARACTERISTIC = "00002a00-0000-1000-8000-00805f9b34fb" //PROPERTY_NOTIFY, PROPERTY_READ
        const val APPEARANCE_CHARACTERISTIC = "00002a01-0000-1000-8000-00805f9b34fb" //PROPERTY_READ
        const val PREFERRED_CONNECTION_PARAMETERS_CHARACTERISTIC = "00002a04-0000-1000-8000-00805f9b34fb" //PROPERTY_READ
    }

    object GenericAttribute {
        const val SERVICE = "00001801-0000-1000-8000-00805f9b34fb"

        const val SERVICE_CHANGED_CHARACTERISTIC = "00002a05-0000-1000-8000-00805f9b34fb" //PROPERTY_INDICATE
        const val CLIENT_CHARACTERISTIC_CONFIGURATION_DESCRIPTOR = "00002902-0000-1000-8000-00805f9b34fb" //PROPERTY_INDICATE
    }

    object DeviceInfo {
        const val SERVICE = "0000180a-0000-1000-8000-00805f9b34fb"

        //Characteristics
        const val METER_ID = "00002a23-0000-1000-8000-00805f9b34fb" //PROPERTY_READ
        const val MODEL_NUMBER = "00002a24-0000-1000-8000-00805f9b34fb" //PROPERTY_READ
        const val SERIAL_NUMBER = "00002a25-0000-1000-8000-00805f9b34fb" //PROPERTY_READ
        const val FIRMWARE_REVISION = "00002a26-0000-1000-8000-00805f9b34fb" //PROPERTY_READ
        const val HARDWARE_REVISION = "00002a27-0000-1000-8000-00805f9b34fb" //PROPERTY_READ
        const val SOFTWARE_REVISION = "00002a28-0000-1000-8000-00805f9b34fb" //PROPERTY_READ
        const val MANUFACTURER_NAME = "00002a29-0000-1000-8000-00805f9b34fb" //PROPERTY_READ
        const val CERTIFICATION_DATA_LIST = "00002a2a-0000-1000-8000-00805f9b34fb" //PROPERTY_READ
        const val PNP_ID = "00002a50-0000-1000-8000-00805f9b34fb" //PROPERTY_READ
    }

    object MainService {
        const val SERVICE = "0000fe60-0000-1000-8000-00805f9b34fb"

        const val WRITE_CHARACTERISTIC = "0000fe61-0000-1000-8000-00805f9b34fb" //PROPERTY_WRITE, PROPERTY_WRITE_NO_RESPONSE
        const val NOTIFY_CHARACTERISTIC = "0000fe62-0000-1000-8000-00805f9b34fb" //PROPERTY_NOTIFY
        const val UNKNOWN_CHARACTERISTIC_1 = "0000fe63-0000-1000-8000-00805f9b34fb" //PROPERTY_NOTIFY, PROPERTY_READ, PROPERTY_WRITE, PROPERTY_WRITE_NO_RESPONSE
        const val UNKNOWN_CHARACTERISTIC_2 = "0000fe64-0000-1000-8000-00805f9b34fb" //PROPERTY_NOTIFY, PROPERTY_READ, PROPERTY_WRITE, PROPERTY_WRITE_NO_RESPONSE

        //Descriptor
        const val CLIENT_CHARACTERISTIC_CONFIGURATION_DESCRIPTOR = "00002902-0000-1000-8000-00805f9b34fb"
        const val USER_DESCRIPTION_DESCRIPTOR = "00002901-0000-1000-8000-00805f9b34fb"

    }

    fun getUUIDFor(value: String) : UUID {
        return UUID.fromString(value)
    }

    fun getDeviceInfoCharacteristics() = listOf(
        DeviceInfo.METER_ID,
        DeviceInfo.MODEL_NUMBER,
        DeviceInfo.MANUFACTURER_NAME,
        DeviceInfo.SERIAL_NUMBER,
        DeviceInfo.FIRMWARE_REVISION,
        DeviceInfo.HARDWARE_REVISION,
        DeviceInfo.SOFTWARE_REVISION,
        DeviceInfo.CERTIFICATION_DATA_LIST,
        DeviceInfo.PNP_ID,
    )
}