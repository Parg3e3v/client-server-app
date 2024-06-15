package com.parg3v.data.extensions

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.parg3v.data.config.DataStoreConfig

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DataStoreConfig.FILE_NAME)
