package com.memtrip.eosreach.db.sharedpreferences

import android.app.Application
import javax.inject.Inject

class RsaEncryptionVerified @Inject constructor(
    application: Application
) : SharedPreferenceItem<Boolean>(application) {

    override val key = "RSA_ENCRYPTION_VERIFIED"

    override fun put(value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    override fun get(): Boolean = prefs.getBoolean(key, false)
}