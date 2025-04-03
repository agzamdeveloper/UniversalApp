package com.example.data.managers

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("times")

    private val timeKey = stringPreferencesKey(TIME_KEY)

    suspend fun saveTime(time: String) {
        context.dataStore.edit { pref ->
            pref[timeKey] = time
        }
    }

    fun getTime() = context.dataStore.data.map { pref ->
        pref[timeKey] ?: DEFAULT_TIME
    }

    private companion object {
        const val DEFAULT_TIME = "00:00:00"
        const val TIME_KEY = "time"
    }
}
