package com.example.blemeter.core.ble.domain.model

enum class ConnectionState(
    val displayName: String
) {
    DISCONNECTED(displayName = "Disconnected"),
    CONNECTING(displayName = "Connecting"),
    CONNECTED(displayName = "Connected"),
    DISCONNECTING(displayName = "Disconnecting"),
}

fun ConnectionState.isConnected() =
    this == ConnectionState.CONNECTED

fun ConnectionState.isDisconnected() =
    this == ConnectionState.DISCONNECTED