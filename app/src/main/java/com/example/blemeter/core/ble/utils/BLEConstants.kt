package com.example.blemeter.core.ble.utils

import java.util.UUID

object BLEConstants {

    const val serviceId = "0000FFF0-0000-1000-8000-00805F9B34FB"
    const val receiveId = "0000FFF1-0000-1000-8000-00805F9B34FB"
    const val sendId = "0000FFF2-0000-1000-8000-00805F9B34FB"

    const val configServiceId = "0000FFF0-0000-1000-8000-00805F9B34FB"
    const val configReceiveId = "0000FFF3-0000-1000-8000-00805F9B34FB"
    const val configSendId = "0000FFF3-0000-1000-8000-00805F9B34FB"

    const val notifyDescriptorId = "00002902-0000-1000-8000-00805f9b34fb"


    

    object Service1 {
        //service 1
        const val service1 = "00001800-0000-1000-8000-00805f9b34fb"
        const val char1 = "00002a00-0000-1000-8000-00805f9b34fb" //PROPERTY_NOTIFY, PROPERTY_READ
        const val char2 = "00002a01-0000-1000-8000-00805f9b34fb" //PROPERTY_READ
        const val char3 = "00002a04-0000-1000-8000-00805f9b34fb" //PROPERTY_READ
    }

    object Service2 {
        //service 2
        const val service2 = "00001801-0000-1000-8000-00805f9b34fb"

        const val char1 = "00002a05-0000-1000-8000-00805f9b34fb" //PROPERTY_INDICATE
        const val desc1 = "00002902-0000-1000-8000-00805f9b34fb" //PROPERTY_INDICATE
    }

    object Service3 {
        const val service = "0000180a-0000-1000-8000-00805f9b34fb"

        const val char1 = "00002a23-0000-1000-8000-00805f9b34fb" //PROPERTY_READ
        const val char2 = "00002a24-0000-1000-8000-00805f9b34fb" //PROPERTY_READ
        const val char3 = "00002a25-0000-1000-8000-00805f9b34fb" //PROPERTY_READ
        const val char4 = "00002a26-0000-1000-8000-00805f9b34fb" //PROPERTY_READ
        const val char5 = "00002a27-0000-1000-8000-00805f9b34fb" //PROPERTY_READ
        const val char6 = "00002a28-0000-1000-8000-00805f9b34fb" //PROPERTY_READ
        const val char7 = "00002a29-0000-1000-8000-00805f9b34fb" //PROPERTY_READ
        const val char8 = "00002a2a-0000-1000-8000-00805f9b34fb" //PROPERTY_READ
        const val char9 = "00002a50-0000-1000-8000-00805f9b34fb" //PROPERTY_READ
    }

    object Service4 {
        const val service = "0000fe60-0000-1000-8000-00805f9b34fb"

        const val char1 = "0000fe61-0000-1000-8000-00805f9b34fb" //PROPERTY_WRITE, PROPERTY_WRITE_NO_RESPONSE
        const val desc1 = "00002902-0000-1000-8000-00805f9b34fb"
        const val desc2 = "00002901-0000-1000-8000-00805f9b34fb"

        const val char2 = "0000fe62-0000-1000-8000-00805f9b34fb" //PROPERTY_NOTIFY
        const val desc3 = "00002902-0000-1000-8000-00805f9b34fb"
        const val desc4 = "00002901-0000-1000-8000-00805f9b34fb"

        const val char3 = "0000fe63-0000-1000-8000-00805f9b34fb" //PROPERTY_NOTIFY, PROPERTY_READ, PROPERTY_WRITE, PROPERTY_WRITE_NO_RESPONSE
        const val desc5 = "00002902-0000-1000-8000-00805f9b34fb"
        const val desc6 = "00002901-0000-1000-8000-00805f9b34fb"

        const val char4 = "0000fe64-0000-1000-8000-00805f9b34fb" //PROPERTY_NOTIFY, PROPERTY_READ, PROPERTY_WRITE, PROPERTY_WRITE_NO_RESPONSE
        const val desc7 = "00002902-0000-1000-8000-00805f9b34fb"
        const val desc8 = "00002901-0000-1000-8000-00805f9b34fb"
    }


    const val MTU = 20

    const val SOF: UByte = 104u //Start of Frame

    const val EOF: UByte = 22u //End of Frame

    const val METER_ADDRESS = "AA5504B10000B5"

    const val COMMAND_RESPONSE_SIZE = 40

    const val STAND_BY: UByte = 0u

    const val IS_CONFIGURATION_MODE = false
}