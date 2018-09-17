package com.memtrip.eosreach.app.account.vote.cast

import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.mxandroid.MxViewState

data class CastVoteViewState(
    val view: View,
    val page: CastVoteFragmentPagerFragment.Page = CastVoteFragmentPagerFragment.Page.PRODUCER
) : MxViewState {

    sealed class View {
        object Idle : View()
        data class Populate(val eosAccount: EosAccount) : View()
    }
}