package com.memtrip.eosreach.db.sharedpreferences

import android.content.Context
import javax.inject.Inject

class UnusedBillingPurchaseId @Inject constructor(
    context: Context
) : SharedPreferenceItem<String>(context) {

    override val key = "UNUSED_ACCOUNT_PURCHASE"

    override fun put(value: String) {
        prefs.edit().putString(key, value).apply()
    }

    override fun get(): String = prefs.getString(key, null)
}