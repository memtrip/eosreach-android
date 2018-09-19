package com.memtrip.eosreach.app.welcome

import com.memtrip.eosreach.db.account.AccountEntity
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class AccountListRenderAction : MxRenderAction {
    object OnProgress : AccountListRenderAction()
    object OnError : AccountListRenderAction()
    object OnRsaEncryptionFailed : AccountListRenderAction()
    object NavigateToSplash : AccountListRenderAction()
    data class NavigateToAccount(val accountEntity: AccountEntity) : AccountListRenderAction()
    object NavigateToAccountList : AccountListRenderAction()
}

interface AccountListViewLayout : MxViewLayout {
    fun showProgress()
    fun navigateToSplash()
    fun showError()
    fun showRsaEncryptionFailed()
    fun navigateToAccount(accountEntity: AccountEntity)
    fun navigateToAccountList()
}

class AccountListViewRenderer @Inject internal constructor() : MxViewRenderer<AccountListViewLayout, EntryViewState> {
    override fun layout(layout: AccountListViewLayout, state: EntryViewState): Unit = when (state.view) {
        EntryViewState.View.Idle -> {
        }
        EntryViewState.View.NavigateToSplash -> {
            layout.navigateToSplash()
        }
        EntryViewState.View.OnProgress -> {
            layout.showProgress()
        }
        EntryViewState.View.OnError -> {
            layout.showError()
        }
        EntryViewState.View.OnRsaEncryptionFailed -> {
            layout.showRsaEncryptionFailed()
        }
        is EntryViewState.View.NavigateToAccount -> {
            layout.navigateToAccount(state.view.accountEntity)
        }
        EntryViewState.View.NavigateToAccountList -> {
            layout.navigateToAccountList()
        }
    }
}