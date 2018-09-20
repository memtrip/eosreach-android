package com.memtrip.eosreach.app.account.actions

import android.app.Application
import com.memtrip.eosreach.api.actions.AccountActionList
import com.memtrip.eosreach.api.actions.AccountActionsRequest
import com.memtrip.eosreach.api.actions.model.AccountAction
import com.memtrip.eosreach.api.balance.ContractAccountBalance

import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class ActionsViewModel @Inject internal constructor(
    private val accountActionsRequest: AccountActionsRequest,
    application: Application
) : MxViewModel<ActionsIntent, ActionsRenderAction, ActionsViewState>(
    ActionsViewState(view = ActionsViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: ActionsIntent): Observable<ActionsRenderAction> = when (intent) {
        ActionsIntent.Idle -> Observable.just(ActionsRenderAction.Idle)
        is ActionsIntent.Init -> getInitialActions(intent.contractAccountBalance, intent.startingPosition)
        is ActionsIntent.Retry -> getInitialActions(intent.contractAccountBalance, intent.startingPosition)
        is ActionsIntent.LoadMoreActions -> getMoreActions(intent.contractAccountBalance, intent.lastItem)
        is ActionsIntent.NavigateToViewAction ->
            Observable.just(ActionsRenderAction.NavigateToViewAction(intent.accountAction))
        is ActionsIntent.NavigateToTransfer ->
            Observable.just(ActionsRenderAction.NavigateToTransfer(intent.contractAccountBalance))
    }

    override fun reducer(previousState: ActionsViewState, renderAction: ActionsRenderAction): ActionsViewState = when (renderAction) {
        ActionsRenderAction.Idle -> previousState.copy(
            view = ActionsViewState.View.Idle)
        ActionsRenderAction.OnProgress -> previousState.copy(
            view = ActionsViewState.View.OnProgress)
        is ActionsRenderAction.OnSuccess -> previousState.copy(
            view = ActionsViewState.View.OnSuccess(renderAction.accountActionList))
        ActionsRenderAction.OnError -> previousState.copy(
            view = ActionsViewState.View.OnError)
        ActionsRenderAction.OnLoadMoreProgress -> previousState.copy(
            view = ActionsViewState.View.OnLoadMoreProgress)
        is ActionsRenderAction.OnLoadMoreSuccess -> previousState.copy(
            view = ActionsViewState.View.OnLoadMoreSuccess(renderAction.accountActionList))
        ActionsRenderAction.OnLoadMoreError -> previousState.copy(
            view = ActionsViewState.View.OnLoadMoreError)
        is ActionsRenderAction.NavigateToViewAction -> previousState.copy(
            view = ActionsViewState.View.NavigateToViewAction(renderAction.accountAction))
        is ActionsRenderAction.NavigateToTransfer -> previousState.copy(
            view = ActionsViewState.View.NavigateToTransfer(renderAction.contractAccountBalance))
    }

    override fun filterIntents(intents: Observable<ActionsIntent>): Observable<ActionsIntent> = Observable.merge(
        intents.ofType(ActionsIntent.Init::class.java).take(1),
        intents.filter {
            !ActionsIntent.Init::class.java.isInstance(it)
        }
    )

    private fun getInitialActions(
        contractAccountBalance: ContractAccountBalance,
        startingPosition: Int
    ): Observable<ActionsRenderAction> {
        return accountActionsRequest.getActionsForAccountName(
            contractAccountBalance,
            -1,
            startingPosition
        ).map {
            if (it.success) {
                ActionsRenderAction.OnSuccess(it.data!!)
            } else {
                ActionsRenderAction.OnError
            }
        }.toObservable().startWith(ActionsRenderAction.OnProgress)
    }

    private fun getMoreActions(
        contractAccountBalance: ContractAccountBalance,
        accountAction: AccountAction
    ): Observable<ActionsRenderAction> {
        return if (accountAction.next > 0) {
            accountActionsRequest.getActionsForAccountName(
                contractAccountBalance,
                accountAction.next - 1,
                -500
            ).map {
                if (it.success) {
                    ActionsRenderAction.OnLoadMoreSuccess(it.data!!)
                } else {
                    ActionsRenderAction.OnLoadMoreError
                }
            }.toObservable().startWith(ActionsRenderAction.OnLoadMoreProgress)
        } else {
            Observable.just(ActionsRenderAction.OnLoadMoreSuccess(AccountActionList(emptyList())))
        }
    }
}