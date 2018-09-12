package com.memtrip.eosreach.db.sharedpreferences

import android.app.Application
import javax.inject.Inject

class SelectedAccount @Inject constructor(
    application: Application
) : SharedPreferenceItem<String>(application) {

    override val key = "SELECTED_ACCOUNT_KEY"

    override fun put(value: String) {
        prefs.edit().putString(key, value).apply()
    }

    override fun get(): String = prefs.getString(key, null)
}