package com.example.blemeter.feature.dashboard.presentation

import com.example.blemeter.config.model.MeterData
import com.example.navigation.BLEMeterNavDestination

data class DashboardUiState(
    val meterData: MeterData = MeterData(),
    val lastSync: Long = 0L,
    val navigationTo: BLEMeterNavDestination? = null
)
