package com.example.blemeter.core.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStore(
    private val context: Context
) {

    companion object {
        private const val METER_ADDRESS_KEY = "meterAddress"
        private const val RECHARGE_TIMES_KEY = "rechargeTimes"
        private val METER_ADDRESS_PREF_KEY = stringPreferencesKey(METER_ADDRESS_KEY)
        private val RECHARGE_TIMES_PREF_KEY = intPreferencesKey(RECHARGE_TIMES_KEY)
    }

    fun getMeterAddress(): Flow<String> = context.dataStore.data
        .map { preference ->
            preference[METER_ADDRESS_PREF_KEY] ?: ""
        }

    suspend fun saveMeterAddress(address: String) {
        context.dataStore.edit { pref ->
            pref[METER_ADDRESS_PREF_KEY] = address
        }
    }

    fun getRechargeTimes(): Flow<Int> = context.dataStore.data
        .map { preference ->
            preference[RECHARGE_TIMES_PREF_KEY] ?: 0
        }

    suspend fun saveRechargeTimes(numberOfTimes: Int) {
        context.dataStore.edit { pref ->
            pref[RECHARGE_TIMES_PREF_KEY] = numberOfTimes
        }
    }
}