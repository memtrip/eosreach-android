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
            val actions = state.view.accountActionList.actions
            if (actions.isEmpty()) {
                layout.showNoActions()
            } else {
                layout.showActions(state.view.accountActionList)
            }
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