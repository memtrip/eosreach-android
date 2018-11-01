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
package com.memtrip.eosreach.app.account.actions

import com.memtrip.eosreach.api.actions.AccountActionList
import com.memtrip.eosreach.api.actions.model.AccountAction
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class ActionsRenderAction : MxRenderAction {
    object Idle : ActionsRenderAction()
    object OnProgress : ActionsRenderAction()
    data class OnSuccess(val accountActionList: AccountActionList) : ActionsRenderAction()
    object NoResults : ActionsRenderAction()
    object OnError : ActionsRenderAction()
    object OnLoadMoreProgress : ActionsRenderAction()
    data class OnLoadMoreSuccess(val accountActionList: AccountActionList) : ActionsRenderAction()
    object OnLoadMoreError : ActionsRenderAction()
    data class NavigateToViewAction(val accountAction: AccountAction) : ActionsRenderAction()
    data class NavigateToTransfer(val contractAccountBalance: ContractAccountBalance) : ActionsRenderAction()
}

interface ActionsViewLayout : MxViewLayout {
    fun showProgress()
    fun showActions(accountActionList: AccountActionList)
    fun showNoActions()
    fun showError()
    fun showLoadMoreProgress()
    fun appendMoreActions(accountActionList: AccountActionList)
    fun showLoadMoreError()
    fun navigateToTransfer(contractAccountBalance: ContractAccountBalance)
    fun navigateToViewAction(accountAction: AccountAction)
}

class ActionsViewRenderer @Inject internal constructor() : MxViewRenderer<ActionsViewLayout, ActionsViewState> {
    override fun layout(layout: ActionsViewLayout, state: ActionsViewState): Unit = when (state.view) {
        ActionsViewState.View.Idle -> {
        }
        ActionsViewState.View.OnProgress -> {
            layout.showProgress()
        }
        is ActionsViewState.View.OnSuccess -> {
            layout.showActions(state.view.accountActionList)
        }
        ActionsViewState.View.NoResults -> {
            layout.showNoActions()
        }
        ActionsViewState.View.OnError -> {
            layout.showError()
        }
        ActionsViewState.View.OnLoadMoreProgress -> {
            layout.showLoadMoreProgress()
        }
        is ActionsViewState.View.OnLoadMoreSuccess -> {
            layout.appendMoreActions(state.view.accountActionList)
        }
        ActionsViewState.View.OnLoadMoreError -> {
            layout.showLoadMoreError()
        }
        is ActionsViewState.View.NavigateToViewAction -> {
            layout.navigateToViewAction(state.view.accountAction)
        }
        is ActionsViewState.View.NavigateToTransfer -> {
            layout.navigateToTransfer(state.view.contractAccountBalance)
        }
    }
}