package com.example.blemeter.core.ble.di

import com.example.blemeter.core.ble.domain.usecases.ParseDescriptor
import com.example.blemeter.core.ble.domain.usecases.ParseRead
import com.example.blemeter.core.ble.domain.usecases.ParseService
import com.example.blemeter.core.ble.domain.usecases.ParseWrite
import com.example.blemeter.core.logger.ExceptionHandler
import com.example.blemeter.core.logger.ILogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    @Singleton
    fun bindParseService(): ParseService =
        ParseService()

    @Provides
    @Singleton
    fun bindParseRead(): ParseRead =
        ParseRead()

    @Provides
    @Singleton
    fun bindParseWrite(
        logger: ILogger,
        exceptionHandler: ExceptionHandler
    ): ParseWrite =
        ParseWrite(logger, exceptionHandler)

    @Provides
    @Singleton
    fun bindParsDescriptor(): ParseDescriptor =
        ParseDescriptor()


}