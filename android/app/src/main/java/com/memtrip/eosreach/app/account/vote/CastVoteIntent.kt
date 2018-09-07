package com.memtrip.eosreach.app.account.vote

import com.memtrip.mxandroid.MxViewIntent

sealed class CastVoteIntent : MxViewIntent {
    object Idle : CastVoteIntent()
    object Init : CastVoteIntent()
    data class Vote(val voterAccountName: String, val blockProducerName: String) : CastVoteIntent()
    object NavigateToBlockProducerList : CastVoteIntent()
    data class ViewLog(val log: String) : CastVoteIntent()
}