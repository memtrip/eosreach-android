package com.memtrip.eosreach.app.manage

import android.content.Context
import android.content.Intent
import com.memtrip.eosreach.app.issue.createaccount.CreateAccountActivity
import dagger.android.AndroidInjection

class ManageCreateAccountActivity : CreateAccountActivity() {

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun success() {
        // finish activity and load Account activity with the new account
    }

    companion object {

        fun manageCreateAccountIntent(context: Context): Intent {
            return Intent(context, ManageCreateAccountActivity::class.java)
        }
    }
}