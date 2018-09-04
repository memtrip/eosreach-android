package com.memtrip.eosreach.db

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.memtrip.eosreach.R
import javax.inject.Inject

class PurgePreferences @Inject internal constructor(
    application: Application
) {

    private val prefs: SharedPreferences = application.getSharedPreferences(
        application.getString(R.string.app_shared_preferences_package),
        Context.MODE_PRIVATE)

    private val keyPrefs: SharedPreferences = application.getSharedPreferences(
        application.getString(R.string.app_keys_shared_preferences_package),
        Context.MODE_PRIVATE)

    fun purgeAll() {
        prefs.edit().clear().apply()
        keyPrefs.edit().clear().apply()
    }
}