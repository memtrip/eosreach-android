package com.memtrip.eosreach.app.account.vote.cast

import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.mxandroid.MxViewIntent

sealed class CastVoteIntent : MxViewIntent {
    object CastProducerVoteTabIdle : CastVoteIntent()
    object CastProxyVoteTabIdle : CastVoteIntent()
    data class Init(val eosAccount: EosAccount) : CastVoteIntent()
}