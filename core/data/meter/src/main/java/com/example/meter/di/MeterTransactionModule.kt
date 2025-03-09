package com.example.meter.di

import com.example.network.ktor.KtorClient
import com.example.meter.data.MeterTransactionRepository
import com.example.meter.data.RemoteDataSource
import com.example.meter.domain.repository.IMeterTransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MeterTransactionModule {

    @Provides
    @Singleton
    internal fun provideMeterTransactionRepository(
        remoteDataSource: RemoteDataSource
    ): IMeterTransactionRepository =
        MeterTransactionRepository(remoteDataSource)

    @Provides
    @Singleton
    internal fun provideRemoteDataSource(
        ktorClient: KtorClient
    ): RemoteDataSource =
        RemoteDataSource(ktorClient)
}