package com.example.local.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreKeys {

    val AUTH_TOKEN_KEY = stringPreferencesKey("AUTH_KEY")
    val REFRESH_TOKEN_KEY = stringPreferencesKey("AUTH_KEY")
}