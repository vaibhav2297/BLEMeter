package com.example.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.local.model.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM userentity WHERE id LIKE :id")
    suspend fun getUser(id: String): UserEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Query("DELETE FROM userentity WHERE id LIKE :id")
    suspend fun deleteUser(id: String)

    @Delete
    suspend fun deleteUser(userEntity: UserEntity)
}