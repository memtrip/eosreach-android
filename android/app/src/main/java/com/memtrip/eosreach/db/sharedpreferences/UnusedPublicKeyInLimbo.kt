package com.memtrip.eosreach.db.sharedpreferences

import android.app.Application
import javax.inject.Inject

class UnusedPublicKeyInLimbo @Inject constructor(
    application: Application
) : SharedPreferenceItem<Boolean>(application) {

    override val key = "UNUSED_PUBLIC_KEY_IN_LIMBO"

    override fun put(value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    override fun get(): Boolean = prefs.getBoolean(key, false)
}