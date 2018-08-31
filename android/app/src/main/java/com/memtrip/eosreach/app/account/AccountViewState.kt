package com.memtrip.eosreach.app.account

import com.memtrip.mxandroid.MxViewState

data class AccountViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        data class OnProgress(val accountName: String) : View()
        data class OnSuccess(val accountView: AccountView) : View()
        object OnErrorFetchingAccount : View()
        object OnErrorFetchingBalances : View()
        object NavigateToAccountList : View()
        object NavigateToImportKey : View()
        object NavigateToCreateAccount : View()
        object NavigateToSettings : View()
    }
}