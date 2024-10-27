package com.example.blemeter.feature.dashboard.di

import com.example.blemeter.core.ble.data.IBLEService
import com.example.blemeter.feature.dashboard.data.repository.DashboardRepository
import com.example.blemeter.feature.dashboard.domain.repository.IDashboardRepository
import com.example.blemeter.feature.dashboard.domain.usecases.AccumulateDataUseCase
import com.example.blemeter.feature.dashboard.domain.usecases.DashboardUseCases
import com.example.blemeter.feature.dashboard.domain.usecases.ObserveDataUseCase
import com.example.blemeter.feature.dashboard.domain.usecases.PurchaseDataUseCase
import com.example.blemeter.feature.dashboard.domain.usecases.ReadMeterDataUseCase
import com.example.blemeter.feature.dashboard.domain.usecases.ValveControlUseCase
import com.example.blemeter.feature.dashboard.domain.usecases.ZeroInitialisationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DashboardModule {

    @Provides
    @Singleton
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

    @Provides
    fun provideObserveUseCase(
        dashboardRepository: IDashboardRepository
    ) = ObserveDataUseCase(dashboardRepository)

    @Provides
    @Singleton
    fun provideDashboardRepository(
        bleService: IBLEService
    ): IDashboardRepository =
        DashboardRepository(
            bleService = bleService
        )
}