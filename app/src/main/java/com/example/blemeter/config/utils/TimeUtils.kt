package com.example.blemeter.config.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object TimeUtils {

    const val DD_MMM_YYYY_HH_MM_A = "dd MMM yyyy, hh:mm a"

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDateAboveO(timestamp: Long, format: String = DD_MMM_YYYY_HH_MM_A): String {
        val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern(format, Locale.getDefault())
        return dateTime.format(formatter)
    }

    private fun formatDateBelowO(timestamp: Long, format: String = DD_MMM_YYYY_HH_MM_A): String {
        val date = Date(timestamp)
        val formatted = SimpleDateFormat(format, Locale.getDefault())
        return formatted.format(date)
    }

    fun formatDate(timestamp: Long, format: String = DD_MMM_YYYY_HH_MM_A): String {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            formatDateAboveO(timestamp, format)
        else
            formatDateBelowO(timestamp, format)
    }
}