package com.example.blemeter.feature.dashboard.presentation

import com.example.blemeter.feature.dashboard.domain.model.MeterControl

sealed interface DashboardUiEvent {

    data class OnMeterControl(val control: MeterControl) : DashboardUiEvent
}