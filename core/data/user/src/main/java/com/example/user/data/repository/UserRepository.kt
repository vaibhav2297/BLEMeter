package com.example.user.data.repository

import com.example.designsystem.utils.flatMap
import com.example.local.datastore.DataStoreKeys
import com.example.local.datastore.IAppDataStore
import com.example.network.stretagy.fetchData
import com.example.user.data.local.LocalDataSource
import com.example.user.data.remote.RemoteDataSource
import com.example.user.domain.model.User
import com.example.user.domain.model.UserProfile
import com.example.user.domain.model.toUser
import com.example.user.domain.repository.IUserRepository
import kotlinx.coroutines.flow.first

internal class UserRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val dataStore: IAppDataStore
) : IUserRepository {

    override suspend fun getUser(isServer: Boolean) = fetchData<Result<User?>> {
        decide { isServer }
        local {
            val users = localDataSource.getUsers().first()
            Result.success(users.firstOrNull()?.toUser())
        }
        server {
            remoteDataSource.getUser().flatMap { userResponse ->
                remoteDataSource.getUserProfile().map { profile ->
                    toUser(
                        userResponse = userResponse,
                        userProfile = profile.firstOrNull() ?: UserProfile()
                    )
                }
            }
        }
        save { result ->
            result.onSuccess { user ->
                user?.let { u ->
                    localDataSource.insertUser(u)
                    dataStore.putPreference(DataStoreKeys.USER_ID_KEY, u.id)
                }
            }
        }
    }

    override suspend fun insertUser(user: User) = fetchData {
        server {
            insertUserProfile(
                userProfile = UserProfile(
                    userId = user.id,
                    isAdmin = user.isAdmin,
                    litersPerRupees = user.litersPerRupees
                )
            )
        }
        save {
            localDataSource.insertUser(user)
        }
    }

    override suspend fun insertUserProfile(userProfile: UserProfile) =
        remoteDataSource.insertUserProfile(userProfile)
}