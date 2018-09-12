package com.memtrip.eosreach.db.sharedpreferences

import android.app.Application
import javax.inject.Inject

class EosPriceLastUpdated @Inject constructor(
    application: Application
) : SharedPreferenceItem<Long>(application) {

    override val key = "EOS_PRICE_LAST_UPDATED"

    override fun put(value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    override fun get(): Long = prefs.getLong(key, -1)
}