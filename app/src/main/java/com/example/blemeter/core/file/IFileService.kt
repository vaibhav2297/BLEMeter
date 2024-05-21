package com.example.blemeter.core.file

import android.net.Uri
import java.io.File

interface IFileService {

    fun getDebugLogDirectory(): File?
    fun getDebugLogFile(fileName: String): File?
    fun writeToFile(file: File, content: String, append: Boolean = true)
    fun deleteAllLogs(): Boolean
    fun deleteAllLogsExceptToday()
    fun getTodayLogFileName(): String
    fun generateDebugLogZip(fileName: String): File?
}