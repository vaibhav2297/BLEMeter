package com.example.local.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreKeys {

    val AUTH_TOKEN_KEY = stringPreferencesKey("AUTH_KEY")
    val REFRESH_TOKEN_KEY = stringPreferencesKey("REFRESH_TOKEN_KEY")
    val USER_ID_KEY = stringPreferencesKey("USER_ID_KEY")
    val USER_LOGGED_IN_KEY = booleanPreferencesKey("USER_LOGGED_IN_KEY")
    val RECHARGE_TIMES_KEY = intPreferencesKey("RECHARGE_TIMES_KEY")
    val METER_ADDRESS_KEY = stringPreferencesKey("METER_ADDRESS_KEY")
    val METER_TYPE_KEY = intPreferencesKey("METER_TYPE_KEY")
    val METER_CALIBRATION_TYPE = intPreferencesKey("METER_CALIBRATION_TYPE")
}