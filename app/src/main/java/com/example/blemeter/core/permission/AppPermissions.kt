package com.example.blemeter.core.permission

import android.Manifest
import android.os.Build

sealed class AppPermissions(val permissions: List<String>) {

    companion object {

        val bluetoothPermissions = if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        ) {
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN
            )
        } else {
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH
            )
        }
    }

    data object BluetoothPermissions : AppPermissions(bluetoothPermissions)
}