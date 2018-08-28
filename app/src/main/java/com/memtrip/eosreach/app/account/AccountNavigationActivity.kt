package com.memtrip.eosreach.app.account

import android.os.Bundle
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.NavigationActivity

class AccountNavigationActivity : NavigationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_navigation_activity)
    }
}