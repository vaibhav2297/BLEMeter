package com.example.blemeter.core.ble.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.os.Build
import android.util.Log
import com.example.blemeter.core.ble.domain.model.ConnectionState
import com.example.blemeter.core.ble.domain.model.DeviceService
import com.example.blemeter.core.ble.domain.model.ScannedDevice
import com.example.blemeter.core.ble.domain.model.toFormattedString
import com.example.blemeter.core.ble.domain.usecases.ParseDescriptor
import com.example.blemeter.core.ble.domain.usecases.ParseRead
import com.example.blemeter.core.ble.domain.usecases.ParseService
import com.example.blemeter.core.ble.domain.usecases.ParseWrite
import com.example.blemeter.core.ble.utils.BLEConstants
import com.example.blemeter.model.Data
import com.example.blemeter.core.ble.utils.toHexString
import com.example.blemeter.core.logger.ExceptionHandler
import com.example.blemeter.core.logger.ILogger
import com.example.blemeter.core.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("MissingPermission")
class BLEGATTService @Inject constructor(
    private val context: Context,
    private val parseService: ParseService,
    private val parseRead: ParseRead,
    private val parseWrite: ParseWrite,
    private val parseDescriptor: ParseDescriptor,
    private val bluetoothAdapter: BluetoothAdapter,
    private val logger: ILogger,
    private val exceptionHandler: ExceptionHandler
) : IBLEGATTService {

    private var btGatt: BluetoothGatt? = null

    /**
     * A Flow that provides a device connection state.
     * @return [ConnectionState]
     */
    private val _connectionState: MutableStateFlow<ConnectionState> by lazy {
        MutableStateFlow(ConnectionState.DISCONNECTED)
    }
    override val connectionState = _connectionState.asStateFlow()

    /**
     * A Flow that provides a services associated with the connected device
     * @return [DeviceService]
     */
    private val _deviceServices: MutableStateFlow<List<DeviceService>> by lazy {
        MutableStateFlow(emptyList())
    }
    override val deviceServices = _deviceServices.asStateFlow()

    /**
     * A Flow that provides a connected device
     * @return [ScannedDevice]
     */
    private val _connectedDevice: MutableStateFlow<ScannedDevice?> by lazy {
        MutableStateFlow(null)
    }
    override val connectedDevice = _connectedDevice.asStateFlow()

    /**
     * A Flow that provides a setting received from BLE
     * @return [Data]
     */
    private val _data: MutableStateFlow<Data?> by lazy {
        MutableStateFlow(null)
    }
    override val data = _data.asStateFlow()

    private fun isBluetoothEnabled() = bluetoothAdapter.isEnabled

    override fun connectToDevice(scannedDevice: ScannedDevice) {
        if (isBluetoothEnabled()) {
            try {
                logger.d("Connecting to device => address : ${scannedDevice.address}  name : ${scannedDevice.deviceName}")
                _connectedDevice.value = scannedDevice
                _connectionState.value = ConnectionState.CONNECTING
                val device = bluetoothAdapter.getRemoteDevice(scannedDevice.address)
                device.connectGatt(context, false, gattCallback, BluetoothDevice.TRANSPORT_LE)
            } catch (e: Exception) {
                exceptionHandler.handle(e)
                _connectedDevice.value = null
                _connectionState.value = ConnectionState.DISCONNECTED
            }
        } else {
            logger.e("Bluetooth is not enabled")
        }
    }

    private val gattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)

            btGatt = gatt

            logger.d("status : $status : newState : $newState")

            when (newState) {
                BluetoothProfile.STATE_CONNECTING -> _connectionState.value =
                    ConnectionState.CONNECTING

                BluetoothProfile.STATE_CONNECTED -> {
                    _connectionState.value = ConnectionState.CONNECTED
                    btGatt?.discoverServices()
                }

                BluetoothProfile.STATE_DISCONNECTING -> _connectionState.value =
                    ConnectionState.DISCONNECTING

                BluetoothProfile.STATE_DISCONNECTED -> {
                    _connectionState.value = ConnectionState.DISCONNECTED
                    _connectedDevice.value = null
                }

                else -> _connectionState.value = ConnectionState.DISCONNECTED
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            logger.d("status : $status")

            _deviceServices.value = emptyList()
            gatt?.let {
                val services = parseService(it, status)
                logger.d("services => \n ${services.toFormattedString()}")
                _deviceServices.value = services
            }

            CoroutineScope(Dispatchers.IO).launch {
                enableNotificationsAndIndications()
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            super.onCharacteristicChanged(gatt, characteristic, value)
            logger.d("UUID : ${characteristic.uuid} : value : ${value.toHexString()}")
            _data.value = parseWrite(characteristic)
        }

        @Suppress("DEPRECATION")
        @Deprecated("Deprecated in Java")
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            super.onCharacteristicChanged(gatt, characteristic)
            logger.d("UUID : ${characteristic?.uuid} : value : ${characteristic?.value?.toHexString()}")
            characteristic?.let { char ->
                _data.value = parseWrite(char)
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, value, status)
            logger.d("UUID : ${characteristic.uuid} : value : ${value.toHexString()}")
            _deviceServices.value = parseRead(_deviceServices.value, characteristic, value, status)
        }

        @Suppress("DEPRECATION")
        @Deprecated("Deprecated in Java")
        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, status)
            logger.d("UUID : ${characteristic?.uuid} : value : ${characteristic?.value?.toHexString()}")
            characteristic?.let { char ->
                _deviceServices.value = parseRead(_deviceServices.value, char, status)
            }
        }

        @Suppress("DEPRECATION")
        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            logger.d("UUID : ${characteristic?.uuid} : value : ${characteristic?.value?.toHexString()}")
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor?,
            status: Int
        ) {
            super.onDescriptorWrite(gatt, descriptor, status)
            logger.d("UUID : ${descriptor?.uuid}")
        }

        override fun onDescriptorRead(
            gatt: BluetoothGatt,
            descriptor: BluetoothGattDescriptor,
            status: Int,
            value: ByteArray
        ) {
            super.onDescriptorRead(gatt, descriptor, status, value)
            logger.d("UUID : ${descriptor.uuid} : value : ${value.toHexString()}")
        }

        @Suppress("DEPRECATION")
        @Deprecated("Deprecated in Java")
        override fun onDescriptorRead(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor?,
            status: Int
        ) {
            super.onDescriptorRead(gatt, descriptor, status)
            logger.d("UUID : ${descriptor?.uuid} : value : ${descriptor?.value?.toHexString()}")
        }
    }

    /**
     * In order to subscribe to notifications on a given characteristic, you must first set the Notifications Enabled bit
     * in its Client Characteristic Configuration Descriptor.
     * Enabling descriptor notification for characteristic
     */
    @Suppress("DEPRECATION")
    suspend fun enableNotificationsAndIndications() {
        btGatt?.services?.forEach { gattSvcForNotify ->
            gattSvcForNotify.characteristics?.forEach { svcChar ->

                svcChar.descriptors.find { desc ->
                    desc.uuid.toString() == BLEConstants.notifyDescriptorId
                }?.also { configDesc ->
                    logger.d("descriptor => UUID : ${configDesc.uuid}")
                    val notifyRegistered = btGatt?.setCharacteristicNotification(svcChar, true)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (svcChar.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0) {
                            logger.d("Writing descriptor for notify => UUID : ${configDesc.uuid}")
                            btGatt?.writeDescriptor(
                                configDesc,
                                BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                            )
                        }

                        if (svcChar.properties and BluetoothGattCharacteristic.PROPERTY_INDICATE > 0) {
                            logger.d("Writing descriptor for indicate => UUID : ${configDesc.uuid}")
                            btGatt?.writeDescriptor(
                                configDesc,
                                BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
                            )
                        }
                    } else {

                        if (svcChar.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0) {
                            logger.d("Writing descriptor for notify => UUID : ${configDesc.uuid}")
                            configDesc.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
                            btGatt?.writeDescriptor(configDesc)
                        }

                        if (svcChar.properties and BluetoothGattCharacteristic.PROPERTY_INDICATE > 0) {
                            logger.d("Writing descriptor for indicate => UUID : ${configDesc.uuid}")
                            configDesc.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE)
                            btGatt?.writeDescriptor(configDesc)
                        }
                    }

                    // give gatt a little breathing room for writes
                    delay(300L)
                }
            }
        }
    }

    override fun readCharacteristics(uuid: String) {
        logger.d("Read to characteristic")
        btGatt?.services?.flatMap { it.characteristics }?.find { svcChar ->
            svcChar.uuid.toString() == uuid
        }?.also { foundChar ->
            logger.d("found characteristic : ${foundChar.uuid}")
            btGatt?.readCharacteristic(foundChar)
        }
    }

    @Suppress("DEPRECATION")
    override fun writeBytes(uuid: String, bytes: ByteArray) {
        try {
            logger.d("Write to characteristic")
            btGatt?.services?.flatMap { it.characteristics }?.find { svcChar ->
                svcChar.uuid.toString() == uuid
            }?.also { foundChar ->
                logger.d("found characteristic : ${foundChar.uuid}")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    btGatt?.writeCharacteristic(
                        foundChar,
                        bytes,
                        BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                    )
                } else {
                    foundChar.value = bytes
                    btGatt?.writeCharacteristic(foundChar)
                }
            }
        } catch (e: Exception) {
            exceptionHandler.handle(e)
            e.printStackTrace()
        }
    }

    override fun close() {
        _connectionState.value = ConnectionState.DISCONNECTING
        try {
            logger.d("closing GATT...")
            if (btGatt != null) {
                btGatt?.also { gatt ->
                    gatt.disconnect()
                    gatt.close()
                    btGatt = null
                    cleanUp()
                }
                _connectionState.value = ConnectionState.DISCONNECTED
            } else {
                logger.d("GATT is null")
            }

        } catch (e: Exception) {
            exceptionHandler.handle(e)
        } finally {
            btGatt = null
        }
    }

    private fun cleanUp() {
        _deviceServices.value = emptyList()
        _data.value = null
        _connectedDevice.value = null
    }

    companion object {
        const val TAG = "BLEGATTService"
    }
}