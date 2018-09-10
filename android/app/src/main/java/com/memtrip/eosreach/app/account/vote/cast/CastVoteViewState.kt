package com.memtrip.eosreach.app.account.vote.cast

import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.mxandroid.MxViewState

data class CastVoteViewState(val view: View) : MxViewState {

    sealed class View {
        object CastProducerVoteTabIdle : View()
        object CastProxyVoteTabIdle : View()
        data class Populate(val eosAccount: EosAccount) : View()
    }
}