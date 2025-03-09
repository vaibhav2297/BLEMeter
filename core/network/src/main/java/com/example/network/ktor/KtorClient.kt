package com.example.network.ktor

import com.example.local.datastore.DataStoreKeys
import com.example.local.datastore.IAppDataStore
import com.example.logger.ILogger
import com.example.network.utils.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import javax.inject.Inject

class KtorClient @Inject constructor(
    private val dataStore: IAppDataStore,
    private val appLogger: ILogger
) {

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
            logger = object : Logger {
                override fun log(message: String) {
                    appLogger.d(message = message)
                }

            }
            level = LogLevel.ALL
        }

        //Retry
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = NetworkConstants.MAX_RETRY)
            exponentialDelay()
        }

        //Authentication
        install(Auth) {
            bearer {
                loadTokens {
                    val authToken =
                        dataStore.getPreference(DataStoreKeys.AUTH_TOKEN_KEY, "").first()
                    val refreshToken =
                        dataStore.getPreference(DataStoreKeys.REFRESH_TOKEN_KEY, "").first()
                    BearerTokens(authToken, refreshToken)
                }

                refreshTokens {
                    val authToken =
                        dataStore.getPreference(DataStoreKeys.AUTH_TOKEN_KEY, "").first()
                    val refreshToken =
                        dataStore.getPreference(DataStoreKeys.REFRESH_TOKEN_KEY, "").first()
                    BearerTokens(authToken, refreshToken)
                }
            }
        }

        //Request
        defaultRequest {
            headers {
                contentType(ContentType.Application.Json)
            }
            header("apiKey", NetworkConstants.API_KEY)
        }

        expectSuccess = true
    }
}