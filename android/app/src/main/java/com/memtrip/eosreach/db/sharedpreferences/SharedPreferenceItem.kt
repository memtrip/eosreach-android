package com.memtrip.eosreach.db.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.memtrip.eosreach.R

abstract class SharedPreferenceItem<T>(
    context: Context,
    internal val prefs: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.app_shared_preferences_package),
        Context.MODE_PRIVATE)
) {
    abstract val key: String
    abstract fun put(value: T)
    abstract fun get(): T

    fun exists(): Boolean = prefs.contains(key)

    fun clear(): Unit = prefs.edit().remove(key).apply()
}