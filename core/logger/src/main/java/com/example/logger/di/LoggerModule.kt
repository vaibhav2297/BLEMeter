package com.example.logger.di

import com.example.file.IFileService
import com.example.logger.ExceptionHandler
import com.example.logger.ILogger
import com.example.logger.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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