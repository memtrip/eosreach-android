package com.memtrip.eosreach.app.welcome.importkey

import android.content.Context
import android.content.Intent
import com.memtrip.eosreach.app.accountlist.AccountListActivity
import com.memtrip.eosreach.app.issue.importkey.ImportKeyActivity
import com.memtrip.eosreach.app.welcome.EntryActivity
import dagger.android.AndroidInjection

class WelcomeImportKeyActivity : ImportKeyActivity() {

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun success() {
        startActivity(EntryActivity.entryIntent(this))
        finish()
    }

    override fun showGithubViewSource(): Boolean = true

    companion object {

        fun welcomeImportKeyIntent(context: Context): Intent {
            return Intent(context, WelcomeImportKeyActivity::class.java)
        }
    }
}