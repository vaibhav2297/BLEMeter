package com.example.user.data.remote

import com.example.network.config.safeRequest
import com.example.network.ktor.KtorClient
import com.example.network.model.SupabaseApis
import com.example.user.domain.model.UserProfile
import com.example.user.domain.model.UserResponse
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import javax.inject.Inject

internal class RemoteDataSource @Inject constructor(
    private val ktorClient: KtorClient
) {

    suspend fun insertUserProfile(
        request: UserProfile
    ): Result<Unit> =
        ktorClient.client.safeRequest<Unit> {
            url(SupabaseApis.USER_PROFILE.url)
            method = HttpMethod.Post
            setBody(request)
        }

    suspend fun getUserProfile(): Result<List<UserProfile>> =
        ktorClient.client.safeRequest<List<UserProfile>> {
            url(SupabaseApis.USER_PROFILE.url)
            method = HttpMethod.Get
        }

    suspend fun getUser(): Result<UserResponse> =
        ktorClient.client.safeRequest<UserResponse> {
            url(SupabaseApis.USER.url)
            method = HttpMethod.Get
        }
}