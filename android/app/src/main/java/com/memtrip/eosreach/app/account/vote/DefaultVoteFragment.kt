package com.memtrip.eosreach.app.account.vote

import dagger.android.support.AndroidSupportInjection

class DefaultVoteFragment : VoteFragment() {

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }
}