package com.example.blemeter.core.ble.di

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.example.blemeter.core.ble.data.BLEGATTService
import com.example.blemeter.core.ble.data.BLEScanService
import com.example.blemeter.core.ble.data.IBLEGATTService
import com.example.blemeter.core.ble.data.IBLEScanService
import com.example.blemeter.core.ble.data.repository.BLEService
import com.example.blemeter.core.ble.data.repository.IBLERepository
import com.example.blemeter.core.ble.domain.repository.BLERepository
import com.example.blemeter.core.ble.domain.usecases.ParseDescriptor
import com.example.blemeter.core.ble.domain.usecases.ParseRead
import com.example.blemeter.core.ble.domain.usecases.ParseService
import com.example.blemeter.core.ble.domain.usecases.ParseWrite
import com.example.blemeter.core.file.IFileService
import com.example.blemeter.core.logger.ExceptionHandler
import com.example.blemeter.core.logger.ILogger
import com.example.blemeter.core.logger.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BleModule {

    @Provides
    @Singleton
    fun bindBLEScanService(
        bluetoothAdapter: BluetoothAdapter,
        logger: ILogger,
        exceptionHandler: ExceptionHandler
    ): IBLEScanService =
        BLEScanService(
            bluetoothAdapter = bluetoothAdapter,
            logger = logger,
            exceptionHandler = exceptionHandler
        )

    @Provides
    @Singleton
    fun bindBLEGATTService(
        @ApplicationContext context: Context,
        parseService: ParseService,
        parseRead: ParseRead,
        parseWrite: ParseWrite,
        parseDescriptor: ParseDescriptor,
        bluetoothAdapter: BluetoothAdapter,
        logger: ILogger,
        exceptionHandler: ExceptionHandler
    ): IBLEGATTService =
        BLEGATTService(
            context = context,
            parseService = parseService,
            parseRead = parseRead,
            parseWrite = parseWrite,
            parseDescriptor = parseDescriptor,
            bluetoothAdapter = bluetoothAdapter,
            logger = logger,
            exceptionHandler = exceptionHandler
        )

    @Provides
    @Singleton
    fun bindBLERepository(
        bleScanService: IBLEScanService,
        bleGattService: IBLEGATTService
    ): IBLERepository =
        BLERepository(bleGattService, bleScanService)

    @Provides
    @Singleton
    fun provideBluetoothManager(
        @ApplicationContext context: Context
    ): BluetoothManager =
        context.getSystemService(BluetoothManager::class.java)

    @Provides
    @Singleton
    fun provideBluetoothAdapter(
        bluetoothManager: BluetoothManager
    ): BluetoothAdapter =
        bluetoothManager.adapter

    @Provides
    @Singleton
    fun bindBLEService(
        scope: CoroutineScope,
        logger: ILogger,
        exceptionHandler: ExceptionHandler
    ): BLEService =
        BLEService(scope,logger, exceptionHandler)
}