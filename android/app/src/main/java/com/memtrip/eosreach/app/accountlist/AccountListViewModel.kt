package com.memtrip.eosreach.app.accountlist

import android.app.Application
import com.memtrip.eosreach.db.account.GetAccounts

import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class AccountListViewModel @Inject internal constructor(
    private val getAccounts: GetAccounts,
    application: Application
) : MxViewModel<AccountListIntent, AccountListRenderAction, AccountListViewState>(
    AccountListViewState(view = AccountListViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: AccountListIntent): Observable<AccountListRenderAction> = when (intent) {
        is AccountListIntent.Init -> getAccounts()
        AccountListIntent.Idle -> Observable.just(AccountListRenderAction.Idle)
        is AccountListIntent.AccountSelected -> Observable.just(AccountListRenderAction.NavigateToAccount(intent.accountName))
    }

    override fun reducer(previousState: AccountListViewState, renderAction: AccountListRenderAction): AccountListViewState = when (renderAction) {
        AccountListRenderAction.Idle -> previousState.copy(view = AccountListViewState.View.Idle)
        AccountListRenderAction.OnProgress -> previousState.copy(view = AccountListViewState.View.OnProgress)
        is AccountListRenderAction.OnSuccess -> previousState.copy(view = AccountListViewState.View.OnSuccess(renderAction.accountList))
        AccountListRenderAction.OnError -> previousState.copy(view = AccountListViewState.View.OnError)
        is AccountListRenderAction.NavigateToAccount -> previousState.copy(view = AccountListViewState.View.NavigateToAccount(renderAction.accountEntity))
    }

    private fun getAccounts(): Observable<AccountListRenderAction> {
        return getAccounts.select().map<AccountListRenderAction> {
            AccountListRenderAction.OnSuccess(it)
        }.toObservable()
            .onErrorReturn { AccountListRenderAction.OnError }
            .startWith(AccountListRenderAction.OnProgress)
    }
}