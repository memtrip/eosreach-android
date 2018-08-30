package com.memtrip.eosreach.app.account

import com.memtrip.mxandroid.MxViewState

data class AccountViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        data class OnSuccess(val accountView: AccountView) : View()
        data class OnErrorFetchingAccount(val accountName: String) : View()
        data class OnErrorFetchingBalances(val accountName: String) : View()
        object NavigateToImportKey : View()
        object NavigateToCreateAccount : View()
        object NavigateToSettings : View()
    }
}