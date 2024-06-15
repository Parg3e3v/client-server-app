package com.parg3v.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.parg3v.data.config.DataStoreConfig
import com.parg3v.data.extensions.dataStore
import com.parg3v.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val context: Context
): DataStoreRepository {
    override suspend fun saveIp(value: String) {
        val preferencesKey = stringPreferencesKey(DataStoreConfig.IP_ADDRESS)
        context.dataStore.edit { preferences -> preferences[preferencesKey] = value }
        Log.d("DATASTORE", "saveIp complete: ($value)")
    }

    override suspend fun getIp(): String? {
        val preferencesKey = stringPreferencesKey(DataStoreConfig.IP_ADDRESS)
        val preferences = context.dataStore.data.first()
        return if (!preferences.contains(preferencesKey)) {
            saveIp(DataStoreConfig.IP_ADDRESS_DEFAULT_VALUE)
            getIp()
        } else {
            preferences[preferencesKey]
        }
    }

    override suspend fun savePortClient(value: String) {
        val preferencesKey = stringPreferencesKey(DataStoreConfig.PORT_CLIENT)
        context.dataStore.edit { preferences -> preferences[preferencesKey] = value }
        Log.d("DATASTORE", "savePortClient complete: ($value)")
    }

    override suspend fun getPortClient(): String? {
        val preferencesKey = stringPreferencesKey(DataStoreConfig.PORT_CLIENT)
        val preferences = context.dataStore.data.first()
        return if (!preferences.contains(preferencesKey)) {
            savePortClient(DataStoreConfig.PORT_CLIENT_DEFAULT_VALUE)
            getPortClient()
        } else {
            preferences[preferencesKey]
        }
    }

    override suspend fun savePortServer(value: String) {
        val preferencesKey = stringPreferencesKey(DataStoreConfig.PORT_SERVER)
        context.dataStore.edit { preferences -> preferences[preferencesKey] = value }
        Log.d("DATASTORE", "savePortServer complete: ($value)")
    }

    override suspend fun getPortServer(): String? {
        val preferencesKey = stringPreferencesKey(DataStoreConfig.PORT_SERVER)
        val preferences = context.dataStore.data.first()
        return if (!preferences.contains(preferencesKey)) {
            savePortServer(DataStoreConfig.PORT_SERVER_DEFAULT_VALUE)
            getPortServer()
        } else {
            preferences[preferencesKey]
        }
    }
}