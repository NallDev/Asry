package com.nalldev.asry.data.datasource.preference

import android.content.SharedPreferences
import javax.inject.Inject

class PreferencesDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        private const val KEY_SESSION = "userSession"
    }

    fun setHasSession(hasSession: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_SESSION, hasSession).apply()
    }

    fun hasSession(): Boolean {
        return sharedPreferences.getBoolean(KEY_SESSION, false)
    }
}