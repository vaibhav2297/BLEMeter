package com.example.blemeter.core.ble.domain.model

enum class ConnectionState(
    val displayName: String
) {
    CONNECTING(displayName = "Connecting"),
    CONNECTED(displayName = "Connected"),
    DISCONNECTING(displayName = "Disconnecting"),
    DISCONNECTED(displayName = "Disconnected")
}

fun ConnectionState.isConnected() =
    this == ConnectionState.CONNECTED

fun ConnectionState.isDisconnected() =
    this == ConnectionState.DISCONNECTED