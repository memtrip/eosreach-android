package com.memtrip.eosreach.app.account.vote

import com.memtrip.eosreach.app.account.AccountTheme
import dagger.android.support.AndroidSupportInjection

class DefaultVoteFragment : VoteFragment() {

    override fun accountTheme(): AccountTheme = AccountTheme.DEFAULT

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }
}