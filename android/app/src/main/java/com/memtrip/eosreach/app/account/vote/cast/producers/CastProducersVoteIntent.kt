package com.memtrip.eosreach.app.account.vote.cast.producers

import com.memtrip.mxandroid.MxViewIntent

sealed class CastProducersVoteIntent : MxViewIntent {
    object Idle : CastProducersVoteIntent()
    object Init : CastProducersVoteIntent()
    data class Vote(val voterAccountName: String, val blockProducers: List<String>) : CastProducersVoteIntent()
    data class AddProducerField(val nextPosition: Int) : CastProducersVoteIntent()
    object NavigateToBlockProducerList : CastProducersVoteIntent()
    data class ViewLog(val log: String) : CastProducersVoteIntent()
}