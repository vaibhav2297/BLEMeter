package com.example.logger

import android.util.Log
import com.example.file.IFileService
import com.example.logger.Constants.IS_ENABLE
import com.example.logger.Constants.IS_FILE_LOG_ENABLE
import com.example.logger.Constants.TAG
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

internal class Logger @Inject constructor(
    private val fileService: IFileService
): ILogger {

    private val dateTimeFormat = SimpleDateFormat.getDateTimeInstance(
        DateFormat.SHORT,
        DateFormat.MEDIUM,
        Locale.getDefault()
    )

    private val dateFormat = SimpleDateFormat(Constants.DEBUG_LOG_DATE_FORMAT, Locale.getDefault())

    private val packageName = Logger::class.java.`package`?.name ?: ""

    override fun d(
        message: String,
        appendDate: Boolean,
        wantFileLog: Boolean
    ) {
        if (IS_ENABLE) {
            Log.d(TAG, message)

            if (wantFileLog && IS_FILE_LOG_ENABLE) {
                val calledMethod = getCallerMethodName()
                val formattedMessage = createLogMessage(message, calledMethod, appendDate)
                writeToFile(formattedMessage)
            }
        }
    }

    override fun e(
        message: String,
        appendDate: Boolean,
        wantFileLog: Boolean
    ) {
        if (IS_ENABLE) {
            Log.e(TAG, message)

            if (wantFileLog && IS_FILE_LOG_ENABLE) {
                val calledMethod = getCallerMethodName()
                val formattedMessage = createLogMessage(message, calledMethod, appendDate)
                writeToFile(formattedMessage)
            }
        }
    }

    override fun i(
        message: String,
        appendDate: Boolean,
        wantFileLog: Boolean
    ) {
        if (IS_ENABLE) {
            Log.i(TAG, message)

            if (wantFileLog && IS_FILE_LOG_ENABLE) {
                val calledMethod = getCallerMethodName()
                val formattedMessage = createLogMessage(message, calledMethod, appendDate)
                writeToFile(formattedMessage)
            }
        }
    }

    private fun createLogMessage(
        message: String,
        calledMethod: String,
        shouldAppendDate: Boolean = true
    ): String {
        return if (shouldAppendDate) {
            getLogDate() + calledMethod + message
        } else {
            message
        }
    }

    private fun getLogDate(): String {
        val date = dateTimeFormat.format(Date())
        return "[$date] : "
    }

    private fun writeToFile(message: String) {
        fileService.getDebugLogFile(getLogFileName())?.let { f ->
            fileService.writeToFile(
                file = f,
                content = message
            )
        }
    }

    private fun getLogFileName(): String {
        return "DebugLog-${dateFormat.format(Date())}.txt"
    }

    private fun getCallerMethodName(): String {
        val stackTrace = Thread.currentThread().stackTrace
        for (i in 4 until stackTrace.size) {
            val stackTraceElement = stackTrace[i]
            if (!stackTraceElement.className.startsWith(packageName)) {
                return "[${stackTraceElement.methodName}] : "
            }
        }
        return "[Unknown] : "
    }

    override fun logDeviceInfo() {
        /*buildString {
            appendLine('\n')
            appendLine('\n')
            appendLine("***************** App Info ******************")
            appendLine("Application Id: ${BuildConfig.APPLICATION_ID}")
            appendLine("Version Name: ${BuildConfig.VERSION_NAME}")
            appendLine("Version Code: ${BuildConfig.VERSION_CODE}")
            appendLine("Min Sdk Version: ${BuildConfig.MIN_SDK_VERSION}")
            appendLine("Build Type: ${BuildConfig.BUILD_TYPE}")
            appendLine("**************** Device Info *****************")
            appendLine("Brand: ${android.os.Build.BRAND}")
            appendLine("Model: ${android.os.Build.MODEL}")
            appendLine("OS: ${android.os.Build.VERSION.SDK_INT}")
            appendLine("**********************************************")
        }.run {
            d(this, appendDate = false)
        }*/
    }
}