package com.memtrip.eosreach.app.accountlist

import com.memtrip.eosreach.app.account.AccountBundle
import com.memtrip.eosreach.db.AccountEntity
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class AccountListRenderAction : MxRenderAction {
    object Idle : AccountListRenderAction()
    object OnProgress : AccountListRenderAction()
    data class OnSuccess(val accountList: List<AccountEntity>) : AccountListRenderAction()
    object OnError : AccountListRenderAction()
    data class NavigateToAccount(
        val accountEntity: AccountEntity
    ) : AccountListRenderAction()
}

interface AccountListViewLayout : MxViewLayout {
    fun showProgress()
    fun showError()
    fun populate(accounts: List<AccountEntity>)
    fun navigateToAccount(accountBundle: AccountBundle)
}

class AccountListViewRenderer @Inject internal constructor() : MxViewRenderer<AccountListViewLayout, AccountListViewState> {
    override fun layout(layout: AccountListViewLayout, state: AccountListViewState): Unit = when (state.view) {
        AccountListViewState.View.Idle -> {
        }
        AccountListViewState.View.OnProgress -> {
            layout.showProgress()
        }
        is AccountListViewState.View.OnSuccess -> {
            layout.populate(state.view.accountList)
        }
        AccountListViewState.View.OnError -> {
            layout.showError()
        }
        is AccountListViewState.View.NavigateToAccount -> {
            layout.navigateToAccount(AccountBundle(
                state.view.accountEntity.accountName,
                state.view.accountEntity.balance,
                state.view.accountEntity.symbol))
        }
    }
}