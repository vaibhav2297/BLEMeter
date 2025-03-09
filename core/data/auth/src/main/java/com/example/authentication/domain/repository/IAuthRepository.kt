package com.example.authentication.domain.repository

import com.example.authentication.domain.model.LoginResponse
import com.example.authentication.domain.model.EmailAuthRequest
import com.example.authentication.domain.model.UserProfileRequest
import com.example.authentication.domain.model.UserResponse

interface IAuthRepository {

    suspend fun signUpWithEmail(
        request: EmailAuthRequest,
        isAutoLogin: Boolean = true
    ): Result<LoginResponse>

    suspend fun loginWithEmail(request: EmailAuthRequest): Result<LoginResponse>

    suspend fun insertUserProfile(request: UserProfileRequest): Result<Unit>
}