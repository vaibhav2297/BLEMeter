package com.example.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.local.model.UserEntity

@Database(
    entities = [UserEntity::class],
    version = AppDatabase.VERSION
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        const val VERSION = 1
        const val NAME = "AppDatabase"
    }
}