package com.example.logger

import javax.inject.Inject

class ExceptionHandler @Inject constructor(
    private val logger: ILogger
) {

    private val packageName = ExceptionHandler::class.java.`package`?.name ?: ""

    fun handle(exception: Exception) {

        logger.e("Exception occurred in method : ${getCallerMethodName()}")
        logger.e(exception.stackTraceToString())
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
}