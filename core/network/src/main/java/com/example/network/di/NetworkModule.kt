package com.example.network.di

import com.example.local.datastore.IAppDataStore
import com.example.logger.ILogger
import com.example.network.ktor.KtorClient
import com.example.network.ktor.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Singleton
    @Provides
    internal fun provideNetworkClient(
        logger: ILogger,
        tokenManager: TokenManager
    ): KtorClient = KtorClient(
        appLogger = logger,
        tokenManager = tokenManager
    )

    @Singleton
    @Provides
    internal fun provideTokenManager(
        dataStore: IAppDataStore
    ): TokenManager = TokenManager(
        dataStore = dataStore
    )
}