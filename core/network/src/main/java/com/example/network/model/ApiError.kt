package com.example.network.model

import io.ktor.http.HttpStatusCode

data class ApiErrorException(
    val code: HttpStatusCode,
    val errorCode: String,
    val msg: String
) : Exception(msg)
