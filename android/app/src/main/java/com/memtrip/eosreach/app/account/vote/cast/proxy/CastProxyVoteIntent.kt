package com.memtrip.eosreach.app.account.vote.cast.proxy

import com.memtrip.mxandroid.MxViewIntent

sealed class CastProxyVoteIntent : MxViewIntent {
    object Init : CastProxyVoteIntent()
}