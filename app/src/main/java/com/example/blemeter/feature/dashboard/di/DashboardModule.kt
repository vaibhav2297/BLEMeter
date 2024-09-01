package com.example.blemeter.feature.dashboard.di

import com.example.blemeter.feature.dashboard.domain.usecases.AccumulateDataUseCase
import com.example.blemeter.feature.dashboard.domain.usecases.DashboardUseCases
import com.example.blemeter.feature.dashboard.domain.usecases.PurchaseDataUseCase
import com.example.blemeter.feature.dashboard.domain.usecases.ReadMeterDataUseCase
import com.example.blemeter.feature.dashboard.domain.usecases.ValveControlUseCase
import com.example.blemeter.feature.dashboard.domain.usecases.ZeroInitialisationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object DashboardModule {

    @Provides
    @ViewModelScoped
    fun provideDashboardUseCases(
        readMeterDataUseCase: ReadMeterDataUseCase,
        valveControlUseCase: ValveControlUseCase,
        purchaseDataUseCase: PurchaseDataUseCase,
        zeroInitialisationUseCase: ZeroInitialisationUseCase,
        accumulateDataUseCase: AccumulateDataUseCase
    ): DashboardUseCases =
        DashboardUseCases(
            readMeterDataUseCase = readMeterDataUseCase,
            valveControlUseCase = valveControlUseCase,
            zeroInitialisationUseCase = zeroInitialisationUseCase,
            purchaseDataUseCase = purchaseDataUseCase,
            accumulateDataUseCase = accumulateDataUseCase
        )
}