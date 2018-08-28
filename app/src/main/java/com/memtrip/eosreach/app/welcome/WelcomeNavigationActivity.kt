package com.memtrip.eosreach.app.welcome

import android.os.Bundle

import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.NavigationActivity

class WelcomeNavigationActivity : NavigationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_navigation_activity)
    }
}