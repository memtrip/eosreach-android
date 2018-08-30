package com.memtrip.eosreach.app.manage

import com.memtrip.eosreach.app.issue.createaccount.CreateAccountFragment

class ManageCreateAccountFragment : CreateAccountFragment() {

    override fun success() {
        // finish activity and load Account activity with the new account
    }
}