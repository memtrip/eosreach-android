package com.memtrip.eosreach.app.account.vote.cast.proxy

import com.memtrip.mxandroid.MxViewState
import com.memtrip.mxandroid.MxViewState.Companion.id

data class CastProxyVoteViewState(
    val view: View,
    val proxyVote: String? = null
) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        data class OnError(val message: String, val log: String, val unique: Int = id()) : View()
        object OnSuccess : View()
        data class ViewLog(val log: String) : View()
    }
}