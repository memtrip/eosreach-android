package com.memtrip.eosreach.app.manage

import android.content.Context
import android.content.Intent
import com.memtrip.eosreach.app.issue.importkey.ImportKeyActivity
import dagger.android.AndroidInjection

class ManageImportKeyActivity : ImportKeyActivity() {

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun success() {
        // finish activity and load account picker
    }

    override fun showGithubViewSource(): Boolean = false

    companion object {

        fun manageImportKeyIntent(context: Context): Intent {
            return Intent(context, ManageImportKeyActivity::class.java)
        }
    }
}