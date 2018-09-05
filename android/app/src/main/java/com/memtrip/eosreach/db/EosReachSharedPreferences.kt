package com.memtrip.eosreach.db

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.memtrip.eosreach.R
import javax.inject.Inject

class SelectedAccount @Inject constructor(
    application: Application
) : Item<String>(application) {

    override val key = "SELECTED_ACCOUNT_KEY"

    override fun put(value: String) {
        prefs.edit().putString(key, value).apply()
    }

    override fun get(): String = prefs.getString(key, null)
}

class EosPriceValue @Inject constructor(
    application: Application
) : Item<Float>(application) {

    override val key = "EOS_PRICE"

    override fun put(value: Float) {
        prefs.edit().putFloat(key, value).apply()
    }

    override fun get(): Float = prefs.getFloat(key, 0f)
}

class EosPriceCurrencyPair @Inject constructor(
    private val application: Application
) : Item<String>(application) {

    override val key = "EOS_PRICE_CURRENCY_PAIR"

    override fun put(value: String) {
        prefs.edit().putString(key, value).apply()
    }

    override fun get(): String = prefs.getString(
        key, application.getString(R.string.app_default_currency))
}

class EosPriceLastUpdated @Inject constructor(
    application: Application
) : Item<Long>(application) {

    override val key = "EOS_PRICE_LAST_UPDATED"

    override fun put(value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    override fun get(): Long = prefs.getLong(key, -1)
}

class EosEndpoint @Inject constructor(
    private val application: Application
) : Item<String>(application) {

    override val key = "EOS_ENDPOINT"

    override fun put(value: String) {
        prefs.edit().putString(key, value).apply()
    }

    override fun get(): String = prefs.getString(
        key, application.getString(R.string.app_default_eos_endpoint_root))
}

abstract class Item<T>(
    application: Application,
    internal val prefs: SharedPreferences = application.getSharedPreferences(
        application.getString(R.string.app_shared_preferences_package),
        Context.MODE_PRIVATE)
) {
    abstract val key: String
    abstract fun put(value: T)
    abstract fun get(): T

    fun exists(): Boolean = prefs.contains(key)

    fun clear(): Unit = prefs.edit().remove(key).apply()
}