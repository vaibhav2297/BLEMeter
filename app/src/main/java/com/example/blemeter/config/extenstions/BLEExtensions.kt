package com.example.blemeter.config.extenstions

import com.juul.kable.State
import com.juul.kable.State.Connected
import com.juul.kable.State.Connecting
import com.juul.kable.State.Disconnected
import com.juul.kable.State.Disconnecting

fun State.isConnected() = this is State.Connected

fun State.isDisconnected() = this is State.Disconnected

fun State.toDisplay() = when (this) {
    Connected -> "Connected"
    Connecting.Bluetooth -> "Initialising"
    Connecting.Observes -> "Observing"
    Connecting.Services -> "Servicing"
    Disconnecting -> "Disconnecting"
    is Disconnected -> "Disconnected"
}