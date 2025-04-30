package com.example.network.ktor

import com.example.local.datastore.DataStoreKeys
import com.example.local.datastore.IAppDataStore
import com.example.network.model.SupabaseApis
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.authProviders
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.plugin
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

class TokenManager @Inject constructor(
    private val dataStore: IAppDataStore
) {

    suspend fun refreshTokens(client: HttpClient): BearerTokens {
        return try {
            val oldRefreshToken =
                dataStore.getPreference(DataStoreKeys.REFRESH_TOKEN_KEY, "").first()

            val response = client.post {
                url("${SupabaseApis.LOGIN.url}?grant_type=refresh_token")
                contentType(ContentType.Application.Json)
                setBody("""{"refresh_token":"$oldRefreshToken"}""")
            }

            val bodyText = response.bodyAsText()
            val json = Json.parseToJsonElement(bodyText).jsonObject

            val newAuthToken = json["access_token"]?.jsonPrimitive?.content.orEmpty()
            val newRefreshToken = json["refresh_token"]?.jsonPrimitive?.content.orEmpty()

            if (newAuthToken.isNotBlank() && newRefreshToken.isNotBlank()) {
                dataStore.putPreference(DataStoreKeys.AUTH_TOKEN_KEY, newAuthToken)
                dataStore.putPreference(DataStoreKeys.REFRESH_TOKEN_KEY, newRefreshToken)
            }
            BearerTokens(newAuthToken, newRefreshToken)
        } catch (e: Exception) {
            e.printStackTrace()
            BearerTokens("", "")
        }
    }

    suspend fun loadTokens(): BearerTokens {
        return try {
            val authToken =
                dataStore.getPreference(DataStoreKeys.AUTH_TOKEN_KEY, "").first()
            val refreshToken =
                dataStore.getPreference(DataStoreKeys.REFRESH_TOKEN_KEY, "").first()

            BearerTokens(authToken, refreshToken)
        } catch (e: Exception) {
            e.printStackTrace()
            BearerTokens("", "")
        }
    }

    object TokenManager {
        fun invalidateAuthToken(client: HttpClient) {
            client.authProviders
                .filterIsInstance<BearerAuthProvider>()
                .firstOrNull()
                ?.clearToken()
        }
    }
}