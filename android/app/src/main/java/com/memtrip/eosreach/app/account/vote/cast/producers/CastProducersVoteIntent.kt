package com.memtrip.eosreach.app.account.vote.cast.producers

import com.memtrip.eosreach.api.account.EosAccountVote
import com.memtrip.mxandroid.MxViewIntent

sealed class CastProducersVoteIntent : MxViewIntent {
    object Idle : CastProducersVoteIntent()
    data class Init(val eosAccountVote: EosAccountVote?) : CastProducersVoteIntent()
    data class Vote(val voterAccountName: String, val blockProducers: List<String>) : CastProducersVoteIntent()
    data class AddProducerField(val nextPosition: Int, val currentTotal: Int) : CastProducersVoteIntent()
    data class RemoveProducerField(val removePosition: Int) : CastProducersVoteIntent()
    data class ViewLog(val log: String) : CastProducersVoteIntent()
}