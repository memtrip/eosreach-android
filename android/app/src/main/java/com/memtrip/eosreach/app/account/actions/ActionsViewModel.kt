package com.memtrip.eosreach.app.account.actions

import android.app.Application
import com.memtrip.eosreach.api.actions.AccountActionsRequest
import com.memtrip.eosreach.api.balance.AccountBalance

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
        is ActionsIntent.Init -> getActions(intent.accountBalance)
        is ActionsIntent.Retry -> getActions(intent.accountBalance)
        is ActionsIntent.NavigateToViewAction -> Observable.just(ActionsRenderAction.NavigateToViewAction(
            intent.accountAction))
    }

    override fun reducer(previousState: ActionsViewState, renderAction: ActionsRenderAction): ActionsViewState = when (renderAction) {
        ActionsRenderAction.Idle -> previousState.copy(view = ActionsViewState.View.Idle)
        ActionsRenderAction.OnProgress -> previousState.copy(view = ActionsViewState.View.OnProgress)
        is ActionsRenderAction.OnSuccess -> previousState.copy(
            view = ActionsViewState.View.OnSuccess(renderAction.accountActionList))
        ActionsRenderAction.OnError -> previousState.copy(view = ActionsViewState.View.OnError)
        is ActionsRenderAction.NavigateToViewAction -> previousState.copy(view = ActionsViewState.View.NavigateToViewAction(
            renderAction.accountAction))
    }

    override fun filterIntents(intents: Observable<ActionsIntent>): Observable<ActionsIntent> = Observable.merge(
        intents.ofType(ActionsIntent.Init::class.java).take(1),
        intents.filter {
            !ActionsIntent.Init::class.java.isInstance(it)
        }
    )

    private fun getActions(accountBalance: AccountBalance): Observable<ActionsRenderAction> {
        return accountActionsRequest.getActionsForAccountName(
            accountBalance.contractName,
            accountBalance.accountName
        ).map {
            if (it.success) {
                ActionsRenderAction.OnSuccess(it.data!!)
            } else {
                ActionsRenderAction.OnError
            }
        }.toObservable().startWith(ActionsRenderAction.OnProgress)
    }
}