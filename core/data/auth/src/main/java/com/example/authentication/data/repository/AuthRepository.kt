package com.example.authentication.data.repository

import com.example.authentication.data.RemoteDataSource
import com.example.authentication.domain.model.EmailAuthRequest
import com.example.authentication.domain.repository.IAuthRepository

internal class AuthRepository(
    private val remoteDataSource: RemoteDataSource
) : IAuthRepository {

    override suspend fun signUpWithEmail(
        request: EmailAuthRequest
    ) = remoteDataSource.signUpWithEmail(request)

    override suspend fun loginWithEmail(
        request: EmailAuthRequest
    ) = remoteDataSource.loginWithEmail(request)
}