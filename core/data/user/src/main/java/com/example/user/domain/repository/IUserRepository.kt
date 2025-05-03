package com.example.user.domain.repository

import com.example.user.domain.model.User
import com.example.user.domain.model.UserProfile

interface IUserRepository {


    suspend fun getUser(isServer: Boolean = false): Result<User?>
    suspend fun insertUser(user: User): Result<Unit>
    suspend fun insertUserProfile(userProfile: UserProfile): Result<Unit>
}