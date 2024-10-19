package com.example.authentication.di

import com.example.authentication.data.RemoteDataSource
import com.example.authentication.data.repository.AuthRepository
import com.example.authentication.domain.repository.IAuthRepository
import com.example.local.datastore.IAppDataStore
import com.example.local.room.UserDao
import com.example.network.ktor.KtorClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AuthModule {

    @Provides
    @Singleton
    internal fun provideAuthRepository(
        remoteDataSource: RemoteDataSource,
        userDao: UserDao
    ): IAuthRepository =
        AuthRepository(remoteDataSource, userDao)

    @Provides
    @Singleton
    internal fun provideRemoteDataSource(
        ktorClient: KtorClient
    ): RemoteDataSource =
        RemoteDataSource(ktorClient)


}