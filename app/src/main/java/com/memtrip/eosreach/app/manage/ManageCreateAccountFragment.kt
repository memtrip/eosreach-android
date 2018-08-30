package com.memtrip.eosreach.app.manage

import com.memtrip.eosreach.app.issue.createaccount.CreateAccountFragment
import dagger.android.support.AndroidSupportInjection

class ManageCreateAccountFragment : CreateAccountFragment() {

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun success() {
        // finish activity and load Account activity with the new account
    }

    companion object {
        fun newInstance(): ManageCreateAccountFragment = ManageCreateAccountFragment()
    }
}