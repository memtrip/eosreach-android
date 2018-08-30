package com.memtrip.eosreach.app.account

import com.memtrip.mxandroid.MxViewIntent

sealed class AccountIntent : MxViewIntent {
    object Idle : AccountIntent()
    data class Init(val accountBundle: AccountBundle) : AccountIntent()
    data class Retry(val accountBundle: AccountBundle) : AccountIntent()
    object NavigateToAccountList : AccountIntent()
    object RefreshAccounts : AccountIntent()
    object NavigateToImportKey : AccountIntent()
    object NavigateToCreateAccount : AccountIntent()
    object NavigateToSettings : AccountIntent()
}