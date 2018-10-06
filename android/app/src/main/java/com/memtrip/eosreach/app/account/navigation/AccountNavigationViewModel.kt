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
package com.memtrip.eosreach.app.account.navigation

import android.app.Application
import com.memtrip.eosreach.db.account.AccountEntity
import com.memtrip.eosreach.db.account.GetAccounts
import com.memtrip.eosreach.db.sharedpreferences.AccountListSelection
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class AccountNavigationViewModel @Inject internal constructor(
    private val getAccounts: GetAccounts,
    private val selectedAccount: AccountListSelection,
    private val accountListUseCase: AccountNavigationUseCase,
    application: Application
) : MxViewModel<AccountNavigationIntent, AccountListRenderAction, AccountNavigationViewState>(
    AccountNavigationViewState(view = AccountNavigationViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: AccountNavigationIntent): Observable<AccountListRenderAction> = when (intent) {
        is AccountNavigationIntent.Init -> getAccounts()
        AccountNavigationIntent.Idle -> Observable.just(AccountListRenderAction.Idle)
        is AccountNavigationIntent.AccountSelected -> accountSelected(intent.accountName)
        AccountNavigationIntent.RefreshAccounts -> refreshAccounts()
        AccountNavigationIntent.NavigateToSettings -> Observable.just(AccountListRenderAction.NavigateToSettings)
    }

    override fun reducer(previousState: AccountNavigationViewState, renderAction: AccountListRenderAction): AccountNavigationViewState = when (renderAction) {
        AccountListRenderAction.Idle -> previousState.copy(
            view = AccountNavigationViewState.View.Idle)
        AccountListRenderAction.OnProgress -> previousState.copy(
            view = AccountNavigationViewState.View.OnProgress)
        is AccountListRenderAction.OnSuccess -> previousState.copy(
            view = AccountNavigationViewState.View.OnSuccess(renderAction.accountList))
        AccountListRenderAction.OnError -> previousState.copy(
            view = AccountNavigationViewState.View.OnError)
        is AccountListRenderAction.NavigateToAccount -> previousState.copy(
            view = AccountNavigationViewState.View.NavigateToAccount(renderAction.accountEntity))
        AccountListRenderAction.NoAccounts -> previousState.copy(
            view = AccountNavigationViewState.View.NoAccounts)
        AccountListRenderAction.NavigateToSettings -> previousState.copy(
            view = AccountNavigationViewState.View.NavigateToSettings)
    }

    override fun filterIntents(intents: Observable<AccountNavigationIntent>): Observable<AccountNavigationIntent> = Observable.merge(
        intents.ofType(AccountNavigationIntent.Init::class.java).take(1),
        intents.filter {
            !AccountNavigationIntent.Init::class.java.isInstance(it)
        }
    )

    private fun getAccounts(): Observable<AccountListRenderAction> {
        return getAccounts.select().map<AccountListRenderAction> { accounts ->
            if (accounts.isEmpty()) {
                AccountListRenderAction.NoAccounts
            } else {
                AccountListRenderAction.OnSuccess(accounts)
            }
        }.toObservable()
    }

    private fun accountSelected(accountEntity: AccountEntity): Observable<AccountListRenderAction> {
        selectedAccount.put(accountEntity.accountName)
        return Observable.just(AccountListRenderAction.NavigateToAccount(accountEntity))
    }

    private fun refreshAccounts(): Observable<AccountListRenderAction> {
        selectedAccount.clear()
        return accountListUseCase
            .refreshAccounts()
            .toObservable()
            .flatMap { result ->
                if (result.success) {
                    getAccounts()
                } else {
                    refreshAccountsError(result.apiError!!)
                }
            }.onErrorReturn { AccountListRenderAction.OnError }
            .startWith(AccountListRenderAction.OnProgress)
    }

    private fun refreshAccountsError(error: AccountNavigationUseCase.AccountsListError): Observable<AccountListRenderAction> = when (error) {
        AccountNavigationUseCase.AccountsListError.RefreshAccountsFailed -> {
            Observable.just(AccountListRenderAction.OnError)
        }
        AccountNavigationUseCase.AccountsListError.NoAccounts -> Observable.just(AccountListRenderAction.NoAccounts)
    }
}