package com.example.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

const val NAME = "AppDataStore"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = NAME)

internal class AppDatastore(
    context: Context
) : IAppDataStore {

    private val datastore = context.dataStore

    /**
     * Retrieves a preference value of a specified type [T] using the provided [key].
     * If the preference value is not found, it returns the [defaultValue].
     *
     * @param key The Preferences.Key used to identify the preference value.
     * @param defaultValue The default value to return if the preference is not set.
     * @return A Flow emitting the preference value or the default value if not set.
     */
    override fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T): Flow<T> =
        datastore.data.catch { e ->
            if (e is IOException)
                emit(emptyPreferences())
            else
                throw e
        }.map { preference ->
            val result = preference[key] ?: defaultValue
            result
        }

    /**
     * Sets a preference value of a specified type [T] using the provided [key] and [value].
     *
     * @param key The Preferences.Key used to identify where to store the preference value.
     * @param value The value to store in the preference.
     */
    override suspend fun <T> putPreference(key: Preferences.Key<T>, value: T) {
        datastore.edit { preference ->
            preference[key] = value
        }
    }

    /**
     * Removes a specific preference entry identified by the provided [key].
     *
     * @param key The Preferences.Key used to identify the preference to be removed.
     */
    override suspend fun <T> removePreference(key: Preferences.Key<T>) {
        datastore.edit { preference ->
            preference.remove(key)
        }
    }

    /**
     * Clears all preference entries, removing all stored key-value pairs in the DataStore.
     */
    override suspend fun clearAllPreference() {
        datastore.edit { preferences ->
            preferences.clear()
        }
    }
}