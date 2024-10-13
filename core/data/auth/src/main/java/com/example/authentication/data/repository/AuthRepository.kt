package com.example.authentication.data.repository

import com.example.authentication.data.RemoteDataSource
import com.example.authentication.domain.model.EmailAuthRequest
import com.example.authentication.domain.model.toUserEntity
import com.example.authentication.domain.repository.IAuthRepository
import com.example.local.room.UserDao

internal class AuthRepository(
    private val remoteDataSource: RemoteDataSource,
    private val dao: UserDao
) : IAuthRepository {

    /**
     * Registers a new user using email authentication. If the sign-up is successful, it automatically
     * logs in the user by calling [loginWithEmail]. If sign-up fails, it returns
     * a failure result with the corresponding exception.
     *
     * @param request The [EmailAuthRequest] containing email and password for sign-up.
     * @return A [Result] indicating the outcome of the sign-up process.
     */
    override suspend fun signUpWithEmail(
        request: EmailAuthRequest
    ) = remoteDataSource.signUpWithEmail(request).fold(
        onSuccess = { loginWithEmail(request) },
        onFailure = { e -> Result.failure(e) }
    )

    /**
     * Logs in an existing user using email authentication.
     * If the login is successful, it saves the user details in the local database.
     *
     * @param request The [EmailAuthRequest] containing email and password for login.
     * @return A [Result] indicating the outcome of the login process.
     */
    override suspend fun loginWithEmail(
        request: EmailAuthRequest
    ) = remoteDataSource.loginWithEmail(request).onSuccess { response ->
        dao.insertUser(response.user.toUserEntity())
    }
}