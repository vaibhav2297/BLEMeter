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

fun String.getMeterAddress(): String {
    check(this.length >= 14 ) { "Not enough size to extract meter address" }

    return this.takeLast(14)
}

fun String.getMeterType(): String {
    check(this.length == 14 ) { "Size of meter address is not proper" }

    return this.substring(8..9)
}