package com.example.blemeter

import com.example.blemeter.config.extenstions.chunkAndReverseString
import com.example.blemeter.config.extenstions.getMeterType
import com.example.blemeter.config.extenstions.isDecimal
import com.example.blemeter.core.ble.domain.model.DataIdentifier
import com.example.blemeter.core.ble.utils.accumulateSum
import com.example.blemeter.core.ble.utils.to4UByteArray
import com.example.blemeter.core.ble.utils.toInt16
import com.example.blemeter.model.BatteryVoltage
import com.example.blemeter.model.MeterAddress
import com.example.blemeter.model.MeterType
import com.example.blemeter.model.ValveStatus
import com.example.blemeter.model.getControlState
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalStdlibApi::class)
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    @Test
    fun assert_meter_request_to_byte_command() {

       /* val meterAddressBytes = ubyteArrayOf()
        val meterAddress = meterAddressBytes.toMeterAddress()


        val baseRequest = BaseRequest(
            meterAddress = meterAddress,
            meterType = meterAddress.meterType
        )*/
    }

    @Test
    fun assert_base_request() {
        /*val meterAddressStr = "LSD4BTC"

        val meterAddressBytes = meterAddressStr.toByteArray(Charsets.UTF_8).toUByteArray()
        val byteAray = ubyteArrayOf(76u, 83u, 68u, 52u, 66u, 84u, 67u)
        val meterAddress = meterAddressBytes.toMeterAddress()

        val actualBaseRequest = BaseRequest(
            meterType = meterAddress.meterType,
            meterAddress = meterAddress,
            serialNumber = 0
        )

        val expectedBaseRequest = BaseRequest(
            meterType = MeterType.ElectricityMeter,
            meterAddress = MeterAddress(
                addressCode = 879986924u,
                meterType = MeterType.ElectricityMeter,
                manufacturerCode = 17236u
            )
        )

        assertEquals(meterAddressBytes.toByteArray().decodeToString(), byteAray.toByteArray().decodeToString())*/

    }

    @OptIn(ExperimentalStdlibApi::class)
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
        val water_valve_close_battery_normal: UByte = 0b00000001u
        val batteryState = BatteryVoltage.NORMAL

        assertEquals(batteryState, BatteryVoltage.getBatteryVoltage(water_valve_close_battery_normal))
    }

    @Test
    fun assert_byte_to_water_valve_close() {
        val water_meter_valve_close_battery_normal: UByte = 0b00000001u
        val controlState = ValveStatus.CLOSE

        assertEquals(controlState, getControlState(MeterType.WaterMeter.ColdWaterMeter, water_meter_valve_close_battery_normal))
    }

    @Test
    fun assert_byte_to_water_valve_open() {
        val water_meter_valve_close_battery_normal: UByte = 0b00000000u
        val controlState = ValveStatus.OPEN

        assertEquals(controlState, getControlState(MeterType.WaterMeter.ColdWaterMeter, water_meter_valve_close_battery_normal))
    }

    @Test
    fun assert_byte_to_water_valve_abnormal() {
        val water_meter_valve_close_battery_normal: UByte = 0b00000101u
        val controlState = ValveStatus.ABNORMAL

        assertEquals(controlState, getControlState(MeterType.WaterMeter.ColdWaterMeter, water_meter_valve_close_battery_normal))
    }

    @Test
    fun assert_byte_to_water_battery_undervolt() {
        val water_meter_valve_open_battery_undervolt: UByte = 0b00000100u
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
        val meterAddress = MeterAddress(addressCode = 876893004u, meterType = MeterType.ElectricityMeter, manufacturerCode =17236u).toUByteArray()
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

    @Test
    fun assert_byteArray_to_float() {
        //val actual = "00000002"
        val actual = "681202001803249671811b901f0000000005ffff".substring(22..25)
        //val expected = "00000002"
        val expected = "901f"
        assertEquals(expected, actual)
    }

    /*@Test
    fun assert_check_code_output() {
        val uByteArray = ubyteArrayOf(0x68u, 0x10u, 0x02u, 0x00u, 0x18u, 0x03u, 0x24u, 0x96u, 0x71u, 0x01u, 0x03u, 0x90u, 0x1Fu, 0xD0u)
        val actual = ValveControlCommand.toCommand(ValveControlRequest(status = ValveInteractionCommand.OPEN))
        val expected: UByte = 0x43u

        assertEquals(expected, actual)
    }*/


    @Test
    fun assert_byteArray_toHexString() {
        val byteArray = listOf<UByte>(104u, 18u, 2u, 0u, 24u, 3u, 36u, 150u, 113u, 129u, 27u, 144u, 31u, 0u, 0u, 0u, 0u, 5u, 255u, 255u, 255u, 251u, 0u, 0u, 0u, 0u, 0u, 2u, 0u, 0u, 0u, 0u, 0u, 5u, 1u, 0u, 0u, 0u, 18u, 22u)

        val expected = DataIdentifier.METER_DATA

        val hexString = byteArray.toUByteArray().toHexString()

        val dataIdentifier = DataIdentifier.getDataType(hexString)

        assertEquals(expected, dataIdentifier)
    }

   /* @Test
    fun assert_valve_command_generation() {
        val actual = ValveControlCommand.toCommand(
            request = ValveControlRequest(
                status = ValveControlCommand.CLOSE,
            )
        ).toHexString(format = HexFormat.UpperCase)

        val expected = Constants.valveOpen

        assertEquals(expected, actual)
    }

    @Test
    fun assert_read_meter_command_generation() {
        val actual = ReadMeterDataCommand.toCommand(
            request = MeterDataRequest(
                baseRequest = BaseRequest(meterAddress = "71962403180002")
            )
        ).toHexString(format = HexFormat.UpperCase)

        val expected = Constants.hexReadMeterData.toUpperCase()

        assertEquals(expected, actual)
    }*/

    @Test
    fun assert_reverse_chunck() {
        val address = "71962403180002"
        val expected = "02001803249671"

        val actual = address.chunkAndReverseString()

        assertEquals(expected, actual)
    }

    @Test
    fun assert_meterType_extract() {
        val address = "71962403180002"
        val expected = "18"

        val actual = address.getMeterType()

        assertEquals(expected, actual)
    }

    @Test
    fun assert_int_to_4byteArray() {
        val expected = ubyteArrayOf(0u,0u,0u,100u)

        val actual = 100u.to4UByteArray()

        assertEquals(expected, actual)
    }

    /*@Test
    fun assert_to_purchase_command() {
        val expected = "6810020018032496710408A013000100000064E416"

        val actual = PurchaseDataCommand.toCommand(
            request = PurchaseDataRequest(
                baseRequest = BaseRequest(meterAddress = "71962403180002"),
                numberTimes = 1u,
                purchaseVariable = 100u
            )
        ).toHexString(format = HexFormat.UpperCase)

        assertEquals(expected, actual)
    }*/

    @Test
    fun assert_string_to_read_command() {
        val data = "681201001803249671811b901f00000000000000006400000064010200000000000501000000db16"
        val expectedAccumulation = "00000000"
        val expectedTotal = "00000064"

        val actualAccumulation = data.substring(28..35)
        val actualTotal = data.substring(44..51)

        val actualStatusByte = data.substring(54..55)
        val expectedStatusByte = "02"

        val alarmVariable = data.substring(58..59)
        val expectedAlarm = "00"

        val overdraft = data.substring(60..61)
        val expectedOverdraft = "00"

        val minimumUsage = data.substring(62..63)
        val expectedMinimum = "00"

        val additionDeduction = data.substring(64..65)
        val expectedAdditionDeduction = "00"

        val productVersion = data.substring(66..67)
        val expectedProductVersion = "05"

        val programVersion = data.substring(68..69)
        val expectedProgramVersion = "01"

        assertEquals(expectedAccumulation, actualAccumulation)
        assertEquals(expectedTotal, actualTotal)
        assertEquals(expectedStatusByte, actualStatusByte)
        assertEquals(expectedMinimum, minimumUsage)
        assertEquals(expectedProductVersion, productVersion)
        assertEquals(expectedProgramVersion, programVersion)
        assertEquals(expectedAdditionDeduction, additionDeduction)
        assertEquals(expectedOverdraft, overdraft)
        assertEquals(expectedAlarm, alarmVariable)
    }

    @Test
    fun assert_string_to_int() {
        val data = "00000064"
        val expected = 100
        val actual = data.toInt(16)

        assertEquals(expected, actual)
    }

    @Test
    fun assert_string_to_isDecimalOnly() {
        val data = "100"
        val expected = false
        val actual = data.isDecimal()

        assertEquals(expected, actual)
    }
}