package com.example.blemeter.core.logger

interface ILogger {

    fun d(message: String, appendDate: Boolean = true, wantFileLog: Boolean = true)
    fun e(message: String, appendDate: Boolean = true, wantFileLog: Boolean = true)
    fun i(message: String, appendDate: Boolean = true, wantFileLog: Boolean = true)
    fun logDeviceInfo()
}