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
package com.memtrip.eosreach.app.account

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class AccountViewModel @Inject internal constructor(
    private val accountUseCase: AccountUseCase,
    application: Application
) : MxViewModel<AccountIntent, AccountRenderAction, AccountViewState>(
    AccountViewState(view = AccountViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: AccountIntent): Observable<AccountRenderAction> = when (intent) {
        is AccountIntent.Init -> getAccount(intent.accountBundle.accountName)
            .startWith(AccountRenderAction.OnProgressWithStartingTab(
                intent.accountBundle.accountName,
                intent.page))
        is AccountIntent.Retry -> getAccount(intent.accountBundle.accountName)
            .startWith(AccountRenderAction.OnProgress(
                intent.accountBundle.accountName))
        is AccountIntent.Refresh -> getAccount(intent.accountBundle.accountName)
            .startWith(AccountRenderAction.OnProgress(
                intent.accountBundle.accountName))
        AccountIntent.BalanceTabIdle -> Observable.just(AccountRenderAction.BalanceTabIdle)
        AccountIntent.ResourceTabIdle -> Observable.just(AccountRenderAction.ResourceTabIdle)
        AccountIntent.VoteTabIdle -> Observable.just(AccountRenderAction.VoteTabIdle)
        AccountIntent.NavigateToAccountList -> Observable.just(AccountRenderAction.NavigateToAccountList)
        AccountIntent.NavigateToImportKey -> Observable.just(AccountRenderAction.NavigateToImportKey)
        AccountIntent.NavigateToCreateAccount -> Observable.just(AccountRenderAction.NavigateToCreateAccount)
        AccountIntent.NavigateToSettings -> Observable.just(AccountRenderAction.NavigateToSettings)
    }

    override fun reducer(previousState: AccountViewState, renderAction: AccountRenderAction): AccountViewState = when (renderAction) {
        AccountRenderAction.BalanceTabIdle -> previousState.copy(
            view = AccountViewState.View.Idle,
            page = AccountFragmentPagerAdapter.Page.BALANCE)
        AccountRenderAction.ResourceTabIdle -> previousState.copy(
            view = AccountViewState.View.Idle,
            page = AccountFragmentPagerAdapter.Page.RESOURCES)
        AccountRenderAction.VoteTabIdle -> previousState.copy(
            view = AccountViewState.View.Idle,
            page = AccountFragmentPagerAdapter.Page.VOTE)
        is AccountRenderAction.OnProgress -> previousState.copy(
            accountName = renderAction.accountName,
            view = AccountViewState.View.OnProgress)
        is AccountRenderAction.OnProgressWithStartingTab -> previousState.copy(
            view = AccountViewState.View.OnProgress,
            accountName = renderAction.accountName,
            page = renderAction.page)
        is AccountRenderAction.OnSuccess -> previousState.copy(
            view = AccountViewState.View.OnSuccess,
            accountView = renderAction.accountView)
        AccountRenderAction.OnErrorFetchingAccount -> previousState.copy(
            view = AccountViewState.View.OnErrorFetchingAccount)
        AccountRenderAction.OnErrorFetchingBalances -> previousState.copy(
            view = AccountViewState.View.OnErrorFetchingAccount)
        AccountRenderAction.NavigateToAccountList -> previousState.copy(
            view = AccountViewState.View.NavigateToAccountList)
        AccountRenderAction.NavigateToImportKey -> previousState.copy(
            view = AccountViewState.View.NavigateToImportKey)
        AccountRenderAction.NavigateToCreateAccount -> previousState.copy(
            view = AccountViewState.View.NavigateToCreateAccount)
        AccountRenderAction.NavigateToSettings -> previousState.copy(
            view = AccountViewState.View.NavigateToSettings)
    }

    override fun filterIntents(intents: Observable<AccountIntent>): Observable<AccountIntent> = Observable.merge(
        intents.ofType(AccountIntent.Init::class.java).take(1),
        intents.filter {
            !AccountIntent.Init::class.java.isInstance(it)
        }
    )

    private fun getAccount(accountName: String): Observable<AccountRenderAction> {
        return accountUseCase.getAccountDetails(
            "eosio.token",
            accountName
        ).map {
            if (it.success) {
                AccountRenderAction.OnSuccess(it)
            } else {
                onError(it.error!!)
            }
        }.toObservable().startWith(AccountRenderAction.OnProgress(accountName))
    }

    private fun onError(error: AccountView.Error): AccountRenderAction = when (error) {
        AccountView.Error.FetchingAccount -> AccountRenderAction.OnErrorFetchingAccount
        AccountView.Error.FetchingBalances -> AccountRenderAction.OnErrorFetchingBalances
    }
}