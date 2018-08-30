package com.memtrip.eosreach.app.accountlist

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class AccountListViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<AccountListIntent, AccountListRenderAction, AccountListViewState>(
    AccountListViewState(view = AccountListViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: AccountListIntent): Observable<AccountListRenderAction> = when (intent) {
        is AccountListIntent.Init -> Observable.just(AccountListRenderAction.OnProgress)
    }

    override fun reducer(previousState: AccountListViewState, renderAction: AccountListRenderAction): AccountListViewState = when (renderAction) {
        AccountListRenderAction.OnProgress -> previousState.copy(view = AccountListViewState.View.OnProgress)
        AccountListRenderAction.OnError -> previousState.copy(view = AccountListViewState.View.OnError)
    }

    override fun filterIntents(intents: Observable<AccountListIntent>): Observable<AccountListIntent> = Observable.merge(
        intents.ofType(AccountListIntent.Init::class.java).take(1),
        intents.filter {
            !AccountListIntent.Init::class.java.isInstance(it)
        }
    )
}