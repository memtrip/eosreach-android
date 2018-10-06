package com.memtrip.eosreach.app.search

import com.memtrip.eosreach.db.account.AccountEntity
import com.memtrip.mxandroid.MxViewState

data class SearchViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        object OnError : View()
        data class OnSuccess(val accountEntity: AccountEntity) : View()
        data class ViewAccount(val accountEntity: AccountEntity) : View()
    }
}