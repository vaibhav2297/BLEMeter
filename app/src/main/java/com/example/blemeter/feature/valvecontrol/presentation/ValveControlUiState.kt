package com.example.blemeter.feature.valvecontrol.presentation

import com.example.blemeter.config.model.ValveStatus

data class ValveControlUiState(
    val valveStatus: ValveStatus = ValveStatus.NONE
)