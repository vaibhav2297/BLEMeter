package com.example.authentication.data

import com.example.authentication.domain.model.LoginResponse
import com.example.authentication.domain.model.EmailAuthRequest
import com.example.authentication.domain.model.UserResponse
import com.example.network.config.safeRequest
import com.example.network.ktor.KtorClient
import com.example.network.model.SupabaseApis
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.HttpMethod

internal class RemoteDataSource {

    suspend fun signUpWithEmail(
        request: EmailAuthRequest
    ): Result<UserResponse> =
        KtorClient.client.safeRequest<UserResponse> {
            url(SupabaseApis.SIGN_UP.url)
            method = HttpMethod.Post
            setBody(request)
        }

    suspend fun loginWithEmail(
        request: EmailAuthRequest
    ): Result<LoginResponse> =
        KtorClient.client.safeRequest<LoginResponse> {
            url(SupabaseApis.SIGN_UP.url)
            method = HttpMethod.Post
            setBody(request)
        }
}
