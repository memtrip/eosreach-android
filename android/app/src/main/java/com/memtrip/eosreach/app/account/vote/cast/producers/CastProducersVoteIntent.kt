package com.memtrip.eosreach.app.account.vote.cast.producers

import com.memtrip.mxandroid.MxViewIntent

sealed class CastProducersVoteIntent : MxViewIntent {
    object Init : CastProducersVoteIntent()
}