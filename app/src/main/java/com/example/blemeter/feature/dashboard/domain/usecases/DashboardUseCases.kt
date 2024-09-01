package com.example.blemeter.feature.dashboard.domain.usecases

data class DashboardUseCases(
    val readMeterDataUseCase: ReadMeterDataUseCase,
    val valveControlUseCase: ValveControlUseCase,
    val zeroInitialisationUseCase: ZeroInitialisationUseCase,
    val purchaseDataUseCase: PurchaseDataUseCase,
    val accumulateDataUseCase: AccumulateDataUseCase
)
