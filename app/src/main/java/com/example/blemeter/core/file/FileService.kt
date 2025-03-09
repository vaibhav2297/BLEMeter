package com.example.blemeter.core.file

import android.content.Context
import com.example.blemeter.config.constants.Extras
import com.example.blemeter.config.constants.Storage
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class FileService(
    private val context: Context,
    private val zipGenerator: IZipGenerator
) : IFileService {


    companion object {
        private const val MAX_FILE_SIZE = 5 * 1024 * 1024 // 5MB
    }

    private val dateFormat = SimpleDateFormat(Extras.DEBUG_LOG_DATE_FORMAT, Locale.getDefault())

    /**
     * creates a App Level Directory
     *
     * e.x BLEMeter
     * */
    private fun createAppDirectoryPath() =
        File(context.filesDir, Storage.AppDataDirectory)

    /**
     * creates a Directory for Debug Logs
     *
     * e.x BLEMeter/DebugLogs
     * */
    override fun getDebugLogDirectory(): File? {
        return try {
            val appDirectory = createAppDirectoryPath()
            File(appDirectory, Storage.DebugLogsDirectory).also { f -> f.mkdirs() }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Retrieves specific file from DebugLog Directory
     * @param [fileName] The name of the file that want to retrieve
     *
     * e.x BLEMeter/DebugLogs/debugLog.txt
     * */
    override fun getDebugLogFile(fileName: String): File? {
        return try {
            val debugLogDirectory = getDebugLogDirectory()
            File(debugLogDirectory, fileName).also { f -> f.createNewFile() }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun generateDebugLogZip(fileName: String) : File? {
        return try {
            val debugLogFile = getDebugLogFile(fileName)
            val debugLogDirectory = getDebugLogDirectory()
            if (debugLogFile != null && debugLogDirectory != null) {
                zipGenerator.createZipFile(debugLogFile, debugLogDirectory)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getDebugLogFiles(): Array<File>? {
        return try {
            getDebugLogDirectory()?.listFiles()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Deletes DebugLogs Folder
     *
     * e.x BLEMeter/DebugLogs
     * */
    override fun deleteAllLogs(): Boolean {
        return try {
            getDebugLogDirectory()?.deleteRecursively() ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun deleteAllLogsExceptToday() {
        try {
            val files = getDebugLogDirectory()?.listFiles { f -> f.name != getTodayLogFileName() }
            files?.map { f -> deleteFile(f) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun deleteFile(file: File): Boolean {
        return try {
            file.delete()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun getTodayLogFileName(): String {
        return "DebugLog-${dateFormat.format(Date())}.txt"
    }

    override fun writeToFile(file: File, content: String, append: Boolean) {
        if (!file.isFileAndExists()) return
        try {
            FileWriter(file, append).use { fs ->
                fs.appendLine(content)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun manageFileSize(file: File) {
        try {
            if (file.length() > MAX_FILE_SIZE) {
                // If the file size exceeds 5 MB, delete the existing file
                file.delete()
                file.createNewFile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun File.isFileAndExists() =
        this.isFile && this.exists()

    private fun File.isEmpty() =
        this.exists() && this.length() == 0L

}