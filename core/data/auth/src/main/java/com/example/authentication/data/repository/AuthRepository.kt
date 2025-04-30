package com.example.authentication.data.repository

import com.example.authentication.data.RemoteDataSource
import com.example.authentication.domain.model.EmailAuthRequest
import com.example.authentication.domain.model.LoginResponse
import com.example.authentication.domain.model.UserProfileRequest
import com.example.authentication.domain.model.toUserEntity
import com.example.authentication.domain.repository.IAuthRepository
import com.example.local.datastore.DataStoreKeys
import com.example.local.datastore.IAppDataStore
import com.example.local.room.UserDao
import com.example.network.stretagy.fetchData

internal class AuthRepository(
    private val remoteDataSource: RemoteDataSource,
    private val dao: UserDao,
    private val dataStore: IAppDataStore
) : IAuthRepository {

    /**
     * Registers a new user using email authentication.
     *
     * This function performs the following steps:
     * 1. Attempts to sign up the user using the provided [EmailAuthRequest] (email and password).
     * 2. If the sign-up is successful:
     *    - Inserts the user's profile into the database using [insertUserProfile].
     *    - Automatically logs in the user by calling [loginWithEmail].
     * 3. If the sign-up fails, it returns a [Result.failure] containing the exception.
     *
     * @param request The [EmailAuthRequest] containing the user's email, password, and admin status.
     * @return A [Result] indicating the outcome of the sign-up process:
     *         - [Result.success] with the logged-in user if both sign-up and login succeed.
     *         - [Result.failure] with the exception if sign-up or login fails.
     */
    override suspend fun signUpWithEmail(
        request: EmailAuthRequest,
        isAutoLogin: Boolean
    ) = remoteDataSource.signUpWithEmail(request).fold(
        onSuccess = { user ->
            insertUserProfile(
                request = UserProfileRequest(
                    userId = user.id,
                    isAdmin = request.isAdmin
                )
            )

            loginWithEmail(request)
        },
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
    ) = fetchData<Result<LoginResponse>> {
        server {
            remoteDataSource.loginWithEmail(request)
        }
        save { result ->
            result.onSuccess { response ->

                //store to Room DB
                dao.insertUser(response.user.toUserEntity())

                //store to preferences
                dataStore.apply {
                    putPreference(DataStoreKeys.AUTH_TOKEN_KEY, response.accessToken)
                    putPreference(DataStoreKeys.REFRESH_TOKEN_KEY, response.refreshToken)
                    putPreference(DataStoreKeys.USER_ID_KEY, response.user.id)
                    putPreference(DataStoreKeys.USER_LOGGED_IN_KEY, true)
                }
            }
        }
    }

    /**
     * Inserts a user profile into the remote data source.
     *
     * @param request The [UserProfileRequest] containing the user's profile information.
     * @return The result of the operation, which could be:
     *         - A success response if the profile is inserted/updated successfully.
     *         - An error response if the operation fails.
     */
    override suspend fun insertUserProfile(
        request: UserProfileRequest
    ) = remoteDataSource.insertUserProfile(request)


    override suspend fun logout(): Result<Unit> {
        return fetchData {
            server {
                remoteDataSource.logout()
            }
            save { result ->
                result.onSuccess {
                    dataStore.clearAllPreference()
                    dao.deleteUserTable()
                }
            }
        }
    }
}
