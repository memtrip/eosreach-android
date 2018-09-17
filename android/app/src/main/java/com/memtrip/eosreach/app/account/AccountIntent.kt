package com.memtrip.eosreach.app.account

import com.memtrip.mxandroid.MxViewIntent

sealed class AccountIntent : MxViewIntent {
    object BalanceTabIdle : AccountIntent()
    object ResourceTabIdle : AccountIntent()
    object VoteTabIdle : AccountIntent()
    data class Init(
        val accountBundle: AccountBundle,
        val page: AccountFragmentPagerAdapter.Page
    ) : AccountIntent()
    data class Retry(val accountBundle: AccountBundle) : AccountIntent()
    data class Refresh(val accountBundle: AccountBundle) : AccountIntent()
    object NavigateToAccountList : AccountIntent()
    object NavigateToImportKey : AccountIntent()
    object NavigateToCreateAccount : AccountIntent()
    object NavigateToSettings : AccountIntent()
}