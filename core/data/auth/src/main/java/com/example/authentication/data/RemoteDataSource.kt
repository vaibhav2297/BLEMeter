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
import javax.inject.Inject

internal class RemoteDataSource @Inject constructor(
    private val ktorClient: KtorClient
) {

    suspend fun signUpWithEmail(
        request: EmailAuthRequest
    ): Result<UserResponse> =
        ktorClient.client.safeRequest<UserResponse> {
            url(SupabaseApis.SIGN_UP.url)
            method = HttpMethod.Post
            setBody(request)
        }

    suspend fun loginWithEmail(
        request: EmailAuthRequest
    ): Result<LoginResponse> =
        ktorClient.client.safeRequest<LoginResponse> {
            url(SupabaseApis.LOGIN.url + "?grant_type=password")
            method = HttpMethod.Post
            setBody(request)
        }
}
