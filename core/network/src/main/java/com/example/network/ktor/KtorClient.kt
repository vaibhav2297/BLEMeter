package com.example.network.ktor

import com.example.network.utils.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorClient {

    val client = HttpClient(Android) {

        engine {
            connectTimeout = NetworkConstants.CONNECTION_TIMEOUT
            socketTimeout = NetworkConstants.SOCKET_TIMEOUT
        }

        //Serialization
        install(ContentNegotiation) {
            json(
                json = Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                }
            )
        }

        //Logging
        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.ALL
        }

        //Retry
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = NetworkConstants.MAX_RETRY)
            exponentialDelay()
        }

        //Request
        defaultRequest {
            headers {
                contentType(ContentType.Application.Json)
            }
        }

        expectSuccess = true
    }
}