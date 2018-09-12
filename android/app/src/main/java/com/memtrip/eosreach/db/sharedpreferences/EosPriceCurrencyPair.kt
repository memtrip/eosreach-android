package com.memtrip.eosreach.db.sharedpreferences

import android.app.Application
import com.memtrip.eosreach.R
import javax.inject.Inject

class EosPriceCurrencyPair @Inject constructor(
    private val application: Application
) : SharedPreferenceItem<String>(application) {

    override val key = "EOS_PRICE_CURRENCY_PAIR"

    override fun put(value: String) {
        prefs.edit().putString(key, value).apply()
    }

    override fun get(): String = prefs.getString(
        key, application.getString(R.string.app_default_currency))
}