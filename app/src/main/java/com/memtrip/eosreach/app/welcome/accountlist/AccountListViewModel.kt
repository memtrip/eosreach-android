package com.memtrip.eosreach.app.welcome.accountlist

import android.app.Application
import com.memtrip.eosreach.db.CountAccounts

import com.memtrip.eosreach.db.GetAccounts
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class AccountListViewModel @Inject internal constructor(
    private val countAccounts: CountAccounts,
    private val getAccounts: GetAccounts,
    application: Application
) : MxViewModel<AccountListIntent, AccountListRenderAction, AccountListViewState>(
    AccountListViewState(view = AccountListViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: AccountListIntent): Observable<AccountListRenderAction> = when (intent) {
        is AccountListIntent.Init -> hasAccounts()
        AccountListIntent.Idle -> Observable.just(AccountListRenderAction.OnProgress)
        is AccountListIntent.AccountSelected -> Observable.just(AccountListRenderAction.NavigateToAccount(intent.accountName))
    }

    override fun reducer(previousState: AccountListViewState, renderAction: AccountListRenderAction): AccountListViewState = when (renderAction) {
        AccountListRenderAction.OnProgress -> previousState.copy(view = AccountListViewState.View.Idle)
        AccountListRenderAction.NavigateToSplash -> previousState.copy(view = AccountListViewState.View.NavigateToSplash)
        is AccountListRenderAction.OnSuccess -> previousState.copy(view = AccountListViewState.View.OnSuccess(renderAction.accounts))
        AccountListRenderAction.OnError -> previousState.copy(view = AccountListViewState.View.OnError)
        is AccountListRenderAction.NavigateToAccount -> previousState.copy(view = AccountListViewState.View.NavigateToAccount(renderAction.accountName))
    }

    private fun hasAccounts(): Observable<AccountListRenderAction> {
        return countAccounts.count()
            .flatMap<AccountListRenderAction> { count ->
                if (count > 0) {
                    getAccounts.select().map {
                        AccountListRenderAction.OnSuccess(it)
                    }
                } else {
                    Single.just(AccountListRenderAction.NavigateToSplash)
                }
            }
            .onErrorReturn { AccountListRenderAction.OnError }
            .toObservable()
            .startWith(AccountListRenderAction.OnProgress)
    }
}