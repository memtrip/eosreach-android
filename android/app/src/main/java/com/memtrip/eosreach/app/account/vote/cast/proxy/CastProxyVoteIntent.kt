package com.memtrip.eosreach.app.account.vote.cast.proxy

import com.memtrip.mxandroid.MxViewIntent

sealed class CastProxyVoteIntent : MxViewIntent {
    object Idle : CastProxyVoteIntent()
    data class Vote(val voterAccountName: String, val proxyAccountName: String) : CastProxyVoteIntent()
    data class ViewLog(val log: String) : CastProxyVoteIntent()
}