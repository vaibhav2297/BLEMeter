package com.example.blemeter.feature.valvecontrol.presentation

import com.example.blemeter.core.ble.domain.model.request.ValveInteractionCommand

sealed interface ValveControlUiEvent {

    data class OnValveControl(val command: ValveInteractionCommand) : ValveControlUiEvent
}