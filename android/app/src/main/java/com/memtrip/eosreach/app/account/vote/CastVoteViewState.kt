package com.memtrip.eosreach.app.account.vote

import com.memtrip.mxandroid.MxViewState

data class CastVoteViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        data class OnError(val message: String, val log: String) : View()
        data class OnSuccess(val blockProducerName: String) : View()
        object NavigateToBlockProducerList : View()
        data class ViewLog(val log: String) : View()
    }
}