package com.memtrip.eosreach.app.account.actions

import com.memtrip.eosreach.api.actions.AccountActionList
import com.memtrip.eosreach.api.actions.model.AccountAction
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class ActionsRenderAction : MxRenderAction {
    object Idle : ActionsRenderAction()
    object OnProgress : ActionsRenderAction()
    data class OnSuccess(val accountActionList: AccountActionList) : ActionsRenderAction()
    object OnError : ActionsRenderAction()
    data class NavigateToViewAction(val accountAction: AccountAction) : ActionsRenderAction()
}

interface ActionsViewLayout : MxViewLayout {
    fun showProgress()
    fun showActions(accountActionList: AccountActionList)
    fun showNoActions()
    fun showError()
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
        is ActionsViewState.View.NavigateToViewAction -> {
            layout.navigateToViewAction(state.view.accountAction)
        }
    }
}