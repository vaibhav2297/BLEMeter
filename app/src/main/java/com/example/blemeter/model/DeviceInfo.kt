package com.example.blemeter.model

data class DeviceInfo(
    val meterId: String = "",
    val modelNumber: String = "",
    val serialNumber: String = "",
    val firmwareRevision: String = "",
    val hardwareRevision: String = "",
    val softwareRevision: String = "",
    val manufacturerName: String = "",
    val certificationDataList: String = "",
    val pnpId: String = "",
)