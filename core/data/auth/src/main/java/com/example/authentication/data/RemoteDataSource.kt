package com.example.authentication.data

import com.example.authentication.domain.model.LoginResponse
import com.example.authentication.domain.model.EmailAuthRequest
import com.example.authentication.domain.model.UserProfileRequest
import com.example.authentication.domain.model.UserResponse
import com.example.network.config.safeRequest
import com.example.network.ktor.KtorClient
import com.example.network.ktor.TokenManager
import com.example.network.model.SupabaseApis
import com.example.user.domain.model.UserProfile
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import javax.inject.Inject

internal class RemoteDataSource @Inject constructor(
    private val ktorClient: KtorClient
) {

    suspend fun signUpWithEmail(
        request: EmailAuthRequest
    ): Result<UserResponse> {
        val response = ktorClient.client.safeRequest<UserResponse> {
            url(SupabaseApis.SIGN_UP.url)
            method = HttpMethod.Post
            setBody(request)
        }
        if (response.isSuccess) {
            TokenManager.TokenManager.invalidateAuthToken(ktorClient.client)
        }
        return response
    }

    suspend fun loginWithEmail(
        request: EmailAuthRequest
    ): Result<LoginResponse> {
        val response = ktorClient.client.safeRequest<LoginResponse> {
            url(SupabaseApis.LOGIN.url + "?grant_type=password")
            method = HttpMethod.Post
            setBody(request)
        }

        if (response.isSuccess) {
            TokenManager.TokenManager.invalidateAuthToken(ktorClient.client)
        }
        return response
    }

    suspend fun insertUserProfile(
        request: UserProfileRequest
    ): Result<Unit> =
        ktorClient.client.safeRequest<Unit> {
            url(SupabaseApis.USER_PROFILE.url)
            method = HttpMethod.Post
            setBody(request)
        }

    suspend fun updateUserProfile(
        request: UserProfileRequest
    ): Result<Unit> =
        ktorClient.client.safeRequest<Unit> {
            url(SupabaseApis.USER_PROFILE.url)
            url {
                parameters.append("user_id", "eq.${request.userId}")
            }
            method = HttpMethod.Patch
            setBody(request)
        }

    suspend fun getUserProfile(): Result<List<UserProfile>> =
        ktorClient.client.safeRequest<List<UserProfile>> {
            url(SupabaseApis.USER_PROFILE.url)
            method = HttpMethod.Get
        }

    suspend fun logout(): Result<Unit> =
        ktorClient.client.safeRequest<Unit> {
            url(SupabaseApis.LOGOUT.url)
            method = HttpMethod.Post
        }
}
