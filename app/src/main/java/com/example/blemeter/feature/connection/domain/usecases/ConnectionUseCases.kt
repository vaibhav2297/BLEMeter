package com.example.blemeter.feature.connection.domain.usecases

import com.example.blemeter.feature.communication.domain.usecases.GetDeviceInfoUseCase

data class ConnectionUseCases(
    val getDeviceInfoUseCase: GetDeviceInfoUseCase
)
