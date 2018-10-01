/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.memtrip.eosreach.app.accountlist

import com.memtrip.eosreach.app.account.AccountBundle
import com.memtrip.eosreach.db.account.AccountEntity

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
    object NoAccounts : AccountListRenderAction()
    object NavigateToSettings : AccountListRenderAction()
}

interface AccountListViewLayout : MxViewLayout {
    fun showProgress()
    fun showError()
    fun populate(accounts: List<AccountEntity>)
    fun navigateToAccount(accountBundle: AccountBundle)
    fun showNoAccounts()
    fun navigateToSettings()
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
            layout.navigateToAccount(AccountBundle(state.view.accountEntity.accountName))
        }
        AccountListViewState.View.NoAccounts -> {
            layout.showNoAccounts()
        }
        AccountListViewState.View.NavigateToSettings -> {
            layout.navigateToSettings()
        }
    }
}