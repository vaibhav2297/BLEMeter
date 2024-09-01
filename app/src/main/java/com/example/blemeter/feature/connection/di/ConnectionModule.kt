package com.example.blemeter.feature.connection.di

import com.example.blemeter.feature.connection.domain.usecases.ConnectionUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ConnectionModule {

    @Provides
    @ViewModelScoped
    fun provideConnectionUseCases(
        getDeviceInfoUseCase: GetDeviceInfoUseCase
    ): ConnectionUseCases =
        ConnectionUseCases(
            getDeviceInfoUseCase = getDeviceInfoUseCase
        )
}