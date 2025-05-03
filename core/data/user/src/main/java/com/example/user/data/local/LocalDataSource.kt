package com.example.user.data.local

import com.example.local.model.UserEntity
import com.example.local.room.UserDao
import com.example.user.domain.model.User
import com.example.user.domain.model.toUserEntity
import javax.inject.Inject

internal class LocalDataSource @Inject constructor(
    private val dao: UserDao
) {

    suspend fun getUsers() =
        dao.getUsers()

    suspend fun getUser(userId: String) =
        dao.getUser(userId)

    suspend fun insertUser(user: User) =
        dao.insertUser(user.toUserEntity())

    suspend fun updateUser(user: User) =
        dao.updateUser(user.toUserEntity())

    suspend fun deleteUser(user: User) =
        dao.deleteUser(user.toUserEntity())

    suspend fun deleteUser(userId: String) =
        dao.deleteUser(userId)

    suspend fun clearUsers() =
        dao.deleteUserTable()
}