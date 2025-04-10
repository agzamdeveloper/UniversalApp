package com.example.data.managers

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
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
    private val darkThemeKey = booleanPreferencesKey(DARK_THEME_KEY)

    suspend fun saveTime(time: String) {
        context.dataStore.edit { pref ->
            pref[timeKey] = time
        }
    }

    suspend fun saveTheme(isDarkTheme: Boolean){
        context.dataStore.edit {pref ->
            pref[darkThemeKey] = isDarkTheme
        }
    }

    fun getTime() = context.dataStore.data.map { pref ->
        pref[timeKey] ?: DEFAULT_TIME
    }

    fun getIsDarkTheme() = context.dataStore.data.map { pref ->
        pref[darkThemeKey] ?: DEFAULT_IS_DARK_THEME
    }

    private companion object {
        const val DEFAULT_TIME = "00:00:00"
        const val TIME_KEY = "time"
        const val DARK_THEME_KEY = "dark_theme"
        const val DEFAULT_IS_DARK_THEME = false
    }
}
