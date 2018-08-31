package com.memtrip.eosreach.db

import android.app.Application
import android.content.Context
import com.memtrip.eosreach.R
import javax.inject.Inject

class EosReachSharedPreferences @Inject constructor(
    application: Application
) {

    private val sharedPreferences = application.getSharedPreferences(
        application.getString(R.string.app_shared_preferences_package),
        Context.MODE_PRIVATE)

    fun getSelectedAccount(): String = sharedPreferences.getString(SELECTED_ACCOUNT_KEY, null)

    fun hasSelectedAccount(): Boolean = sharedPreferences.contains(SELECTED_ACCOUNT_KEY)

    fun setSelectedAccount(accountName: String) {
        sharedPreferences.edit().putString(SELECTED_ACCOUNT_KEY, accountName).apply()
    }

    companion object {
        private const val SELECTED_ACCOUNT_KEY = "SELECTED_ACCOUNT_KEY"
    }
}