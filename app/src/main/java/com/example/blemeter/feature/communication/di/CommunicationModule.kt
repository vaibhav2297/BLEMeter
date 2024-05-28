package com.example.blemeter.feature.communication.di

import com.example.blemeter.feature.communication.domain.usecases.CommunicationUseCases
import com.example.blemeter.feature.communication.domain.usecases.GetDeviceInfoUseCase
import com.example.blemeter.feature.communication.domain.usecases.ReadMeterDataUseCase
import com.example.blemeter.feature.communication.domain.usecases.ValveControlUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object CommunicationModule {

    @Provides
    @ViewModelScoped
    fun provideCommunicationUseCases(
        readMeterDataUseCase: ReadMeterDataUseCase,
        valveControlUseCase: ValveControlUseCase,
        getDeviceInfoUseCase: GetDeviceInfoUseCase
    ): CommunicationUseCases =
        CommunicationUseCases(
            readMeterDataUseCase = readMeterDataUseCase,
            valveControlUseCase = valveControlUseCase,
            getDeviceInfoUseCase = getDeviceInfoUseCase
        )
}