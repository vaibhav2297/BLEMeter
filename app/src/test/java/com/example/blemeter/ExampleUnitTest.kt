package com.example.blemeter

import android.util.Log
import com.example.blemeter.core.ble.domain.bleparsable.MeterDataCommand
import com.example.blemeter.core.ble.domain.model.DataIdentifier
import com.example.blemeter.core.ble.domain.model.request.BaseRequest
import com.example.blemeter.core.ble.domain.model.request.MeterDataRequest
import com.example.blemeter.core.ble.utils.accumulateSum
import com.example.blemeter.core.ble.utils.toHexString
import com.example.blemeter.core.ble.utils.toInt16
import com.example.blemeter.model.BatteryVoltage
import com.example.blemeter.model.MeterAddress
import com.example.blemeter.model.MeterAddress.Companion.toMeterAddress
import com.example.blemeter.model.MeterType
import com.example.blemeter.model.Statuses
import com.example.blemeter.model.ValveStatus
import com.example.blemeter.model.getControlState
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    @Test
    fun assert_meter_request_to_byte_command() {

        val meterAddressBytes = byteArrayOf()
        val meterAddress = meterAddressBytes.toMeterAddress()


        val baseRequest = BaseRequest(
            meterAddress = meterAddress,
            meterType = meterAddress.meterType
        )
    }

    @Test
    fun assert_base_request() {
        val meterAddressStr = "LSD4BTC"

        val meterAddressBytes = meterAddressStr.toByteArray(Charsets.UTF_8)
        val byteAray = byteArrayOf(76, 83, 68, 52, 66, 84, 67)
        val meterAddress = meterAddressBytes.toMeterAddress()

        val actualBaseRequest = BaseRequest(
            meterType = meterAddress.meterType,
            meterAddress = meterAddress,
            serialNumber = 0
        )

        val expectedBaseRequest = BaseRequest(
            meterType = MeterType.ElectricityMeter,
            meterAddress = MeterAddress(
                addressCode = 879986924,
                meterType = MeterType.ElectricityMeter,
                manufacturerCode = 17236
            )
        )

        assertEquals(meterAddressBytes.decodeToString(), byteAray.decodeToString())

    }

    @Test
    fun assert_string_to_bytearray() {
        val string = "LSD4BTC"
        val byteArray = byteArrayOf(76, 83, 68, 52, 66, 84, 67)

        assertEquals(byteArray.toHexString(), string.toByteArray(Charsets.UTF_8).toHexString())
    }

    fun assert_bytearray_to_metertype() {
        val meterAddressStr = "LSD4BTC"
        val byteArray = byteArrayOf(76, 83, 68, 52, 66, 84, 67)

        val meterAddressBytes = meterAddressStr.toByteArray(Charsets.UTF_8).toUByteArray()

        val address = "LSD4"
        val meterType = MeterType.ElectricityMeter

    }

    @Test
    fun assert_byte_to_water_battery_normal() {
        val water_valve_close_battery_normal: Byte = 0b00000001
        val batteryState = BatteryVoltage.NORMAL

        assertEquals(batteryState, BatteryVoltage.getBatteryVoltage(water_valve_close_battery_normal))
    }

    @Test
    fun assert_byte_to_water_valve_close() {
        val water_meter_valve_close_battery_normal: Byte = 0b00000001
        val controlState = ValveStatus.CLOSE

        assertEquals(controlState, getControlState(MeterType.WaterMeter.ColdWaterMeter, water_meter_valve_close_battery_normal))
    }

    @Test
    fun assert_byte_to_water_valve_open() {
        val water_meter_valve_close_battery_normal: Byte = 0b00000000
        val controlState = ValveStatus.OPEN

        assertEquals(controlState, getControlState(MeterType.WaterMeter.ColdWaterMeter, water_meter_valve_close_battery_normal))
    }

    @Test
    fun assert_byte_to_water_valve_abnormal() {
        val water_meter_valve_close_battery_normal: Byte = 0b00000101
        val controlState = ValveStatus.ABNORMAL

        assertEquals(controlState, getControlState(MeterType.WaterMeter.ColdWaterMeter, water_meter_valve_close_battery_normal))
    }

    @Test
    fun assert_byte_to_water_battery_undervolt() {
        val water_meter_valve_open_battery_undervolt: Byte = 0b00000100
        val batteryVoltage = BatteryVoltage.NORMAL

        assertEquals(batteryVoltage, BatteryVoltage.getBatteryVoltage(water_meter_valve_open_battery_undervolt))
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun assert_byte_to_data_identifier() {
        val meterData = DataIdentifier.METER_DATA
        val meterHex = "901F"
        val byteArray = byteArrayOf(0,0,0,0,0,0,0,0,0,0,-112, 31,0)


        assertEquals(36895, byteArray.toInt16(11))
    }

    @Test
    fun assert_meter_address_to_byte_array() {
        val meterAddress = MeterAddress(addressCode=876893004, meterType= MeterType.ElectricityMeter, manufacturerCode=17236).toByteArray()
        val size = 7

        assertEquals(size,meterAddress.size)
    }

    @Test
    fun assert_acuumulate_sum() {
        val command =  byteArrayOf(0x68, 0x40, 0x4c, 0x53, 0x44, 0x34, 0x40, 0x54, 0x43, 0x01, 0x03,
            0x90.toByte(), 0x1f, 0x00)

        val actualCheckCode = command.accumulateSum(0, command.size - 1)

        val expectedCheckCode: Byte = 73 // low byte of 841
        assertEquals(expectedCheckCode,actualCheckCode)
    }

}