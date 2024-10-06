package com.example.network.config

import com.example.network.model.ApiErrorException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend inline fun <reified T> HttpClient.safeRequest(
    block: HttpRequestBuilder.() -> Unit
): Result<T> = try {
    val response = request { block() }
    Result.success(response.body())
} catch (e: ResponseException) {
    Result.failure(e.toApiErrorException())
} catch (e: IOException) {
    Result.failure(e)
} catch (e: SerializationException) {
    Result.failure(e)
}

suspend fun ResponseException.toApiErrorException(): ApiErrorException {

    try {
        val errorText = response.bodyAsText()
        val jsonObject = Json.parseToJsonElement(errorText).jsonObject

        val messageText = when {
            jsonObject.containsKey("msg") -> jsonObject["msg"]?.jsonPrimitive?.contentOrNull
                ?: "Unknown error"

            jsonObject.containsKey("message") -> jsonObject["message"]?.jsonPrimitive?.contentOrNull
                ?: "Unknown error"

            else -> "Unknown error"
        }

        val errorCode = when {
            jsonObject.containsKey("error_code") -> jsonObject["error_code"]?.jsonPrimitive?.contentOrNull
                ?: ""

            else -> ""
        }

        return ApiErrorException(
            code = response.status, errorCode = errorCode, msg = messageText
        )
    } catch (e: Exception) {
        return ApiErrorException(
            code = response.status, errorCode = "", msg = e.message ?: "Unknown error"
        )
    }
}


