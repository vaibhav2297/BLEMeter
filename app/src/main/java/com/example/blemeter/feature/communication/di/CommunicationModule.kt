package com.example.blemeter.feature.communication.di

import com.example.blemeter.feature.communication.domain.usecases.AccumulateDataUseCase
import com.example.blemeter.feature.communication.domain.usecases.CommunicationUseCases
import com.example.blemeter.feature.communication.domain.usecases.GetDeviceInfoUseCase
import com.example.blemeter.feature.communication.domain.usecases.PurchaseDataUseCase
import com.example.blemeter.feature.communication.domain.usecases.ReadMeterDataUseCase
import com.example.blemeter.feature.communication.domain.usecases.ValveControlUseCase
import com.example.blemeter.feature.communication.domain.usecases.ZeroInitialisationUseCase
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
        getDeviceInfoUseCase: GetDeviceInfoUseCase,
        purchaseDataUseCase: PurchaseDataUseCase,
        zeroInitialisationUseCase: ZeroInitialisationUseCase,
        accumulateDataUseCase: AccumulateDataUseCase
    ): CommunicationUseCases =
        CommunicationUseCases(
            readMeterDataUseCase = readMeterDataUseCase,
            valveControlUseCase = valveControlUseCase,
            getDeviceInfoUseCase = getDeviceInfoUseCase,
            purchaseDataUseCase = purchaseDataUseCase,
            zeroInitialisationUseCase = zeroInitialisationUseCase,
            accumulateDataUseCase = accumulateDataUseCase
        )
}