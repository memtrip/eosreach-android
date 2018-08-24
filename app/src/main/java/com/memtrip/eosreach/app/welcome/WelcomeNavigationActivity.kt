package com.memtrip.eosreach.app.welcome

import android.os.Bundle

import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.NavigationActivity
import dagger.android.AndroidInjection

class WelcomeNavigationActivity : NavigationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_navigation_activity)
    }
}