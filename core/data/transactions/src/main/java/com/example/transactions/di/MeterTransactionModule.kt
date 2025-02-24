package com.example.transactions.di

import com.example.network.ktor.KtorClient
import com.example.transactions.data.MeterTransactionRepository
import com.example.transactions.data.RemoteDataSource
import com.example.transactions.domain.repository.IMeterTransactionRepository
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