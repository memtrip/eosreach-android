package com.memtrip.eosreach.app.account

import com.memtrip.mxandroid.MxViewState

data class AccountViewState(
    val view: View,
    val accountName: String? = null,
    val accountView: AccountView? = null,
    val page: AccountFragmentPagerAdapter.Page = AccountFragmentPagerAdapter.Page.BALANCE
) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        object OnSuccess : View()
        object OnErrorFetchingAccount : View()
        object OnErrorFetchingBalances : View()
        object NavigateToAccountList : View()
        object NavigateToImportKey : View()
        object NavigateToCreateAccount : View()
        object NavigateToSettings : View()
    }
}