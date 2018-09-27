package com.memtrip.eosreach.db.sharedpreferences

import android.app.Application
import javax.inject.Inject

class AccountListSelection @Inject constructor(
    application: Application
) : SharedPreferenceItem<String>(application) {

    override val key = "ACCOUNT_LIST_SELECTION"

    override fun put(value: String) {
        prefs.edit().putString(key, value).apply()
    }

    override fun get(): String = prefs.getString(key, null)
}