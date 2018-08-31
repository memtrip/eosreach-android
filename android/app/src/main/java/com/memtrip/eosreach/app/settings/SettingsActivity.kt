package com.memtrip.eosreach.app.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.memtrip.eosreach.app.NavigationActivity

class SettingsActivity : NavigationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {

        fun settingsIntent(context: Context): Intent = Intent(context, SettingsActivity::class.java)
    }
}