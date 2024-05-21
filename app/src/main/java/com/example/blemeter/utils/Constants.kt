package com.example.blemeter.utils

import androidx.compose.runtime.traceEventEnd

object Storage {

    const val AppDataDirectory = "BleMeter"

    const val DebugLogsDirectory = "DebugLogs"

    const val DebugLogFile = "DebugLogs.txt"

    const val DebugLogZip = "DebugLogs.zip"
}

object Extras {

    const val MIME_TEXT_FILE = "text/plain"

    const val MIME_ALL = "*/*"

    const val DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss"

    const val DEBUG_LOG_DATE_FORMAT = "ddMMyyyy"
}

object Logger {

    const val TAG = "BLE_METER"

    const val IS_ENABLE = true

    const val IS_FILE_LOG_ENABLE = true
}

object ShakeDetectorConstant {

    const val IS_SHAKE_DETECTOR_ENABLED = true

    // Adjust the shake threshold value as needed
    const val SHAKE_THRESHOLD = 25.0
}


