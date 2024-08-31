package com.example.blemeter.feature.communication.presentation

import com.example.blemeter.core.ble.domain.model.request.AccumulateDataRequest
import com.example.blemeter.core.ble.domain.model.request.PurchaseDataRequest
import com.example.blemeter.core.ble.domain.model.request.ValveControlCommandStatus

sealed interface CommunicationUiEvent {

    data object OnMeterDataRead : CommunicationUiEvent

    data class OnValveInteraction(
        val valveControlCommandStatus: ValveControlCommandStatus
    ) : CommunicationUiEvent

    data class OnPurchaseData(val request: PurchaseDataRequest) : CommunicationUiEvent

    data object OnZeroInitialise : CommunicationUiEvent

    data class OnAccumulateData(val request: AccumulateDataRequest) : CommunicationUiEvent
}