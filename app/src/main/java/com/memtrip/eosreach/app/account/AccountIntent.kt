package com.memtrip.eosreach.app.account

import com.memtrip.mxandroid.MxViewIntent

sealed class AccountIntent : MxViewIntent {
    object Init : AccountIntent()
    object Idle : AccountIntent()
    object Retry : AccountIntent()
    object RefreshAccounts : AccountIntent()
    object NavigateToImportKey : AccountIntent()
    object NavigateToCreateAccount : AccountIntent()
    object NavigateToSettings : AccountIntent()
}