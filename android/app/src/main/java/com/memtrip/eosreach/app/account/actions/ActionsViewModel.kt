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

import android.app.Application
import com.memtrip.eosreach.api.actions.AccountActionList
import com.memtrip.eosreach.api.actions.AccountActionsRequest
import com.memtrip.eosreach.api.actions.model.AccountAction
import com.memtrip.eosreach.api.balance.ContractAccountBalance

import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import io.reactivex.Single
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
        is ActionsIntent.Init -> getInitialActions(intent.contractAccountBalance)
            .toObservable().startWith(ActionsRenderAction.OnProgress)
        is ActionsIntent.Retry -> getInitialActions(intent.contractAccountBalance)
            .toObservable().startWith(ActionsRenderAction.OnProgress)
        is ActionsIntent.LoadMoreActions -> getMoreActions(intent.contractAccountBalance, intent.lastItem)
            .toObservable().startWith(ActionsRenderAction.OnLoadMoreProgress)
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
        ActionsRenderAction.NoResults -> previousState.copy(
            view = ActionsViewState.View.NoResults)
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
        position: Long = -1,
        recursivePosition: Long = 0
    ): Single<ActionsRenderAction> {
        if (recursivePosition >= RECURSIVE_LIMIT) {
            return Single.just(ActionsRenderAction.NoResults)
        } else {
            return accountActionsRequest.getActionsForAccountName(
                contractAccountBalance,
                position,
                ITEM_OFFSET
            ).flatMap { result ->
                if (result.success) {
                    val results = result.data!!
                    if (results.actions.isEmpty()) {
                        if (results.noResultsNext > 0) {
                            getInitialActions(
                                contractAccountBalance,
                                results.noResultsNext,
                                recursivePosition + 1)
                        } else {
                            Single.just(ActionsRenderAction.NoResults)
                        }
                    } else {
                        Single.just(ActionsRenderAction.OnSuccess(results))
                    }
                } else {
                    Single.just(ActionsRenderAction.OnError)
                }
            }
        }
    }

    private fun getMoreActions(
        contractAccountBalance: ContractAccountBalance,
        lastAccountActionItem: AccountAction,
        recursivePosition: Long = 0
    ): Single<ActionsRenderAction> {
        if (recursivePosition >= RECURSIVE_LIMIT) {
            // end
            return Single.just(ActionsRenderAction.OnLoadMoreSuccess(AccountActionList(emptyList())))
        } else {
            return if (lastAccountActionItem.next > 0) {
                accountActionsRequest.getActionsForAccountName(
                    contractAccountBalance,
                    lastAccountActionItem.next - 1,
                    ITEM_OFFSET
                ).flatMap { result ->
                    if (result.success) {
                        val results = result.data!!
                        if (results.actions.isEmpty()) {
                            if (results.noResultsNext > 0) {
                                getMoreActions(
                                    contractAccountBalance,
                                    lastAccountActionItem,
                                    recursivePosition + 1
                                )
                            } else {
                                Single.just(ActionsRenderAction.OnLoadMoreSuccess(results))
                            }
                        } else {
                            // end
                            Single.just(ActionsRenderAction.OnLoadMoreSuccess(AccountActionList(emptyList())))
                        }
                    } else {
                        Single.just(ActionsRenderAction.OnLoadMoreError)
                    }
                }
            } else {
                // end
                Single.just(ActionsRenderAction.OnLoadMoreSuccess(AccountActionList(emptyList())))
            }
        }
    }

    companion object {
        const val ITEM_OFFSET = -1000L
        const val RECURSIVE_LIMIT = 10L
    }
}