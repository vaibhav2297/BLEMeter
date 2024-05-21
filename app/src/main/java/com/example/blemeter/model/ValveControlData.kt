package com.example.blemeter.model

import com.example.blemeter.core.ble.domain.model.request.ValveControlCommandStatus

data class ValveControlData(
    val valveControlCommandStatus: ValveControlCommandStatus = ValveControlCommandStatus.NONE
) : Data
