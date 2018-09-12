package com.memtrip.eosreach.db.sharedpreferences

import android.app.Application
import javax.inject.Inject

class EosPriceValue @Inject constructor(
    application: Application
) : SharedPreferenceItem<Float>(application) {

    override val key = "EOS_PRICE"

    override fun put(value: Float) {
        prefs.edit().putFloat(key, value).apply()
    }

    override fun get(): Float = prefs.getFloat(key, 0f)
}