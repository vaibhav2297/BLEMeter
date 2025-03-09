package com.example.blemeter

import android.app.Application
import com.example.blemeter.core.file.IFileService
import com.example.logger.ILogger
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BLEMeterApplication : Application() {

    @Inject
    lateinit var logger: ILogger

    @Inject
    lateinit var fileService: IFileService

    override fun onCreate() {
        super.onCreate()

        //Logging device info on every startup
        logger.logDeviceInfo()

        //deleting old logs
        fileService.deleteAllLogsExceptToday()
    }
}