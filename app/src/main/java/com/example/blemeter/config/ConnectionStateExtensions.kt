package com.example.blemeter.config

import com.juul.kable.State

fun State.isConnected() = this is State.Connected

fun State.isDisconnected() = this is State.Disconnected