package com.example.local.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.local.datastore.AppDatastore
import com.example.local.datastore.IAppDataStore
import com.example.local.room.AppDatabase
import com.example.local.room.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object LocalModule {

    @Singleton
    @Provides
    internal fun provideAppDataStore(
        @ApplicationContext context: Context
    ): IAppDataStore = AppDatastore(context)

    @Singleton
    @Provides
    internal fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context = context,
        klass = AppDatabase::class.java,
        name = AppDatabase.NAME
    ).build()

    @Singleton
    @Provides
    fun provideUserDao(
        db: AppDatabase
    ): UserDao = db.userDao()
}