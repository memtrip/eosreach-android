package com.memtrip.eosreach.app.welcome.createaccount

import android.content.Context
import android.content.Intent
import com.memtrip.eosreach.app.issue.createaccount.CreateAccountActivity

import dagger.android.AndroidInjection

class WelcomeCreateAccountActivity : CreateAccountActivity() {

    override fun inject() {
        AndroidInjection.inject(this)
    }

    companion object {

        fun welcomeCreateAccountIntent(context: Context): Intent {
            return Intent(context, WelcomeCreateAccountActivity::class.java)
        }
    }
}