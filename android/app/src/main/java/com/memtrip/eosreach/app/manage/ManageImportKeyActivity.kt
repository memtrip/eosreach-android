package com.memtrip.eosreach.app.manage

import android.content.Context
import android.content.Intent
import android.view.Menu
import com.memtrip.eosreach.app.issue.importkey.ImportKeyActivity
import com.memtrip.eosreach.app.welcome.EntryActivity.Companion.entryIntent
import dagger.android.AndroidInjection

class ManageImportKeyActivity : ImportKeyActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true // do not inflate the settings menu item
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun success() {
        startActivity(entryIntent(this))
        finish()
    }

    override fun showGithubViewSource(): Boolean = false

    companion object {

        fun manageImportKeyIntent(context: Context): Intent {
            return Intent(context, ManageImportKeyActivity::class.java)
        }
    }
}