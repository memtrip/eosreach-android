package com.memtrip.eosreach.app.account.actions

import com.memtrip.eosreach.api.actions.AccountActionList
import com.memtrip.eosreach.api.actions.model.AccountAction
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.mxandroid.MxViewState

data class ActionsViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        data class OnSuccess(val accountActionList: AccountActionList) : View()
        object OnError : View()
        object OnLoadMoreProgress : View()
        data class OnLoadMoreSuccess(val accountActionList: AccountActionList) : View()
        object OnLoadMoreError : View()
        data class NavigateToViewAction(val accountAction: AccountAction) : View()
        data class NavigateToTransfer(val contractAccountBalance: ContractAccountBalance) : View()
    }
}