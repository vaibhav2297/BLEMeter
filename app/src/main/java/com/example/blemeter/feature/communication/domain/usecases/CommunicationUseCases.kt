package com.example.blemeter.feature.communication.domain.usecases

data class CommunicationUseCases(
    val valveControlUseCase: ValveControlUseCase,
    val readMeterDataUseCase: ReadMeterDataUseCase,
    val getDeviceInfoUseCase: GetDeviceInfoUseCase,
    val purchaseDataUseCase: PurchaseDataUseCase,
    val zeroInitialisationUseCase: ZeroInitialisationUseCase,
    val accumulateDataUseCase: AccumulateDataUseCase
)
