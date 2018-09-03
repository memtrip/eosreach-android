package com.memtrip.eosreach.app.welcome.importkey

import android.content.Context
import android.content.Intent
import com.memtrip.eosreach.app.accountlist.AccountListActivity
import com.memtrip.eosreach.app.issue.importkey.ImportKeyActivity
import dagger.android.AndroidInjection

class WelcomeImportKeyActivity : ImportKeyActivity() {

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun success() {
        startActivity(with (AccountListActivity.accountListIntent(this)) {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        })
        finish()
    }

    override fun showGithubViewSource(): Boolean = true

    companion object {

        fun welcomeImportKeyIntent(context: Context): Intent {
            return Intent(context, WelcomeImportKeyActivity::class.java)
        }
    }
}