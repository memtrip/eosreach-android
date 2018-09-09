package com.memtrip.eosreach.app.account.vote

import com.memtrip.eosreach.api.account.EosAccountVote
import com.memtrip.mxandroid.MxViewIntent

sealed class VoteIntent : MxViewIntent {
    object Idle : VoteIntent()
    data class Init(val eosAccountVote: EosAccountVote?) : VoteIntent()
}