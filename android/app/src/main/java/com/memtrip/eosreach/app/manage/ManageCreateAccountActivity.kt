package com.memtrip.eosreach.app.manage

import android.content.Context
import android.content.Intent
import com.memtrip.eosreach.app.issue.createaccount.CreateAccountActivity
import com.memtrip.eosreach.app.welcome.EntryActivity
import dagger.android.AndroidInjection

class ManageCreateAccountActivity : CreateAccountActivity() {

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun success() {
        startActivity(EntryActivity.entryIntent(this))
        finish()
    }

    companion object {

        fun manageCreateAccountIntent(context: Context): Intent {
            return Intent(context, ManageCreateAccountActivity::class.java)
        }
    }
}