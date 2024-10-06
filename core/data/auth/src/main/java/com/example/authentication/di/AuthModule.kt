package com.example.authentication.di

import com.example.authentication.data.RemoteDataSource
import com.example.authentication.data.repository.AuthRepository
import com.example.authentication.domain.repository.IAuthRepository
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
        remoteDataSource: RemoteDataSource
    ): IAuthRepository =
        AuthRepository(remoteDataSource)

    @Provides
    @Singleton
    internal fun provideRemoteDataSource(): RemoteDataSource =
        RemoteDataSource()


}