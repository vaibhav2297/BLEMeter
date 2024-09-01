package com.example.blemeter.feature.dashboard.presentation

import com.example.blemeter.model.MeterData

data class DashboardUiState(
    val meterData: MeterData = MeterData(),
    val lastSync: Long = 0L
)
