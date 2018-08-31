package com.memtrip.eosreach.app.welcome.createaccount

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import com.memtrip.eosreach.app.accountlist.AccountListActivity.Companion.accountListIntent
import com.memtrip.eosreach.app.issue.createaccount.CreateAccountActivity
import dagger.android.AndroidInjection

class WelcomeCreateAccountActivity : CreateAccountActivity() {

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun success() {
        startActivity(with (accountListIntent(this)) {
            addFlags(FLAG_ACTIVITY_CLEAR_TASK)
        })
        finish()
    }

    companion object {

        fun welcomeCreateAccountIntent(context: Context): Intent {
            return Intent(context, WelcomeCreateAccountActivity::class.java)
        }
    }
}