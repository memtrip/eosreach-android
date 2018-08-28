package com.memtrip.eosreach.app.welcome.accountlist

import android.app.Application
import com.memtrip.eosreach.storage.EosReachSharedPreferences
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class AccountListViewModel @Inject internal constructor(
    private val eosReachSharedPreferences: EosReachSharedPreferences,
    application: Application
) : MxViewModel<AccountListIntent, AccountListRenderAction, AccountListViewState>(
    AccountListViewState(view = AccountListViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: AccountListIntent): Observable<AccountListRenderAction> = when (intent) {
        is AccountListIntent.Init -> hasAccounts()
        AccountListIntent.Idle -> Observable.just(AccountListRenderAction.OnProgress)
    }

    override fun reducer(previousState: AccountListViewState, renderAction: AccountListRenderAction): AccountListViewState = when (renderAction) {
        AccountListRenderAction.OnProgress -> previousState.copy(view = AccountListViewState.View.Idle)
        AccountListRenderAction.NavigateToSplash -> previousState.copy(view = AccountListViewState.View.NavigateToSplash)
        AccountListRenderAction.OnSuccess -> previousState.copy(view = AccountListViewState.View.OnSuccess)
        AccountListRenderAction.OnError -> previousState.copy(view = AccountListViewState.View.OnError)
    }

    private fun hasAccounts(): Observable<AccountListRenderAction> {
        return eosReachSharedPreferences
            .accountCreated()
            .map<AccountListRenderAction> { hasAccounts ->
                if (hasAccounts) {
                    AccountListRenderAction.OnSuccess
                } else {
                    AccountListRenderAction.NavigateToSplash
                }
            }
            .onErrorReturn { AccountListRenderAction.OnError }
            .toObservable()
            .startWith(AccountListRenderAction.OnProgress)
    }
}