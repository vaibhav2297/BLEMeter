package com.example.blemeter.core.logger.di

import android.content.Context
import com.example.blemeter.core.file.FileService
import com.example.blemeter.core.file.IFileService
import com.example.blemeter.core.logger.ExceptionHandler
import com.example.blemeter.core.logger.ILogger
import com.example.blemeter.core.logger.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoggerModule {

    @Provides
    @Singleton
    fun bindLogger(
        fileService: IFileService
    ): ILogger =
        Logger(fileService)

    @Provides
    @Singleton
    fun bindExceptionHandler(
        logger: ILogger
    ): ExceptionHandler =
        ExceptionHandler(logger)
}