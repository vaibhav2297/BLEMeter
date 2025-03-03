package com.example.wallet.di

import com.example.wallet.data.LocalDataSource
import com.example.wallet.data.WalletRepository
import com.example.wallet.data.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object WalletModule {

    @Provides
    @Singleton
    internal fun provideWalletRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): com.example.wallet.domain.repository.WalletRepository =
        WalletRepository(remoteDataSource, localDataSource)

}