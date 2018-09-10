package com.memtrip.eosreach.app.account.vote.cast.proxy

import com.memtrip.mxandroid.MxViewState

data class CastProxyVoteViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        data class OnError(val message: String, val log: String) : View()
        object OnSuccess : View()
        data class ViewLog(val log: String) : View()
    }
}