package com.example.authentication.domain.repository

import com.example.authentication.domain.model.LoginResponse
import com.example.authentication.domain.model.EmailAuthRequest
import com.example.authentication.domain.model.UserResponse

interface IAuthRepository {
    suspend fun signUpWithEmail(request: EmailAuthRequest): Result<UserResponse>
    suspend fun loginWithEmail(request: EmailAuthRequest): Result<LoginResponse>
}