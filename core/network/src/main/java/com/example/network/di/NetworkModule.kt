package com.example.network.di

import com.example.local.datastore.IAppDataStore
import com.example.network.ktor.KtorClient
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
        dataStore: IAppDataStore
    ): KtorClient = KtorClient(dataStore)
}