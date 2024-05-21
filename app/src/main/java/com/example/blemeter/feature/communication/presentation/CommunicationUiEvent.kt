package com.example.blemeter.feature.communication.presentation

import com.example.blemeter.core.ble.domain.model.request.ValveControlCommandStatus

sealed interface CommunicationUiEvent {

    data object OnMeterDataRead : CommunicationUiEvent

    data class OnValveInteraction(
        val valveControlCommandStatus: ValveControlCommandStatus
    ) : CommunicationUiEvent
}