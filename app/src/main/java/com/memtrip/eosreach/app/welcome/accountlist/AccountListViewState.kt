package com.memtrip.eosreach.app.welcome.accountlist

import com.memtrip.eosreach.db.AccountEntity
import com.memtrip.mxandroid.MxViewState

data class AccountListViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object NavigateToSplash : View()
        object OnProgress : View()
        data class OnSuccess(val accounts: List<AccountEntity>) : View()
        object OnError : View()
        data class NavigateToAccount(val accountName: String) : View()
    }
}