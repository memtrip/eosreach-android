package com.memtrip.eosreach.db.sharedpreferences

import android.app.Application

import javax.inject.Inject

class UnusedBillingPurchaseToken @Inject constructor(
    application: Application
) : SharedPreferenceItem<String>(application) {

    override val key = "UNUSED_ACCOUNT_PURCHASE"

    override fun put(value: String) {
        prefs.edit().putString(key, value).apply()
    }

    override fun get(): String = prefs.getString(key, "")
}