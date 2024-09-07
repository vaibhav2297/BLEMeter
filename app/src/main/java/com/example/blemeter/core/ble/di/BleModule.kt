package com.example.blemeter.core.ble.di

import com.example.blemeter.core.ble.data.BLEService
import com.example.blemeter.core.ble.data.IBLEService
import com.example.blemeter.core.logger.ExceptionHandler
import com.example.blemeter.core.logger.ILogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BleModule {

    @Provides
    @Singleton
    fun bindBLEService(
        scope: CoroutineScope,
        logger: ILogger,
        exceptionHandler: ExceptionHandler
    ): BLEService =
        BLEService(scope,logger, exceptionHandler)

    @Provides
    @Singleton
    fun bindIBLEService(
        scope: CoroutineScope,
        logger: ILogger,
        exceptionHandler: ExceptionHandler
    ): IBLEService =
        BLEService(scope,logger, exceptionHandler)
}