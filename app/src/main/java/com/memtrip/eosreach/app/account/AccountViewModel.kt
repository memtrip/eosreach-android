package com.memtrip.eosreach.app.account

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class AccountViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<AccountIntent, AccountRenderAction, AccountViewState>(
    AccountViewState(view = AccountViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: AccountIntent): Observable<AccountRenderAction> = when (intent) {
        is AccountIntent.Init -> Observable.just(AccountRenderAction.OnProgress)
    }

    override fun reducer(previousState: AccountViewState, renderAction: AccountRenderAction): AccountViewState = when (renderAction) {
        AccountRenderAction.OnProgress -> previousState.copy(view = AccountViewState.View.OnProgress)
        AccountRenderAction.OnError -> previousState.copy(view = AccountViewState.View.OnError)
    }

    override fun filterIntents(intents: Observable<AccountIntent>): Observable<AccountIntent> = Observable.merge(
        intents.ofType(AccountIntent.Init::class.java).take(1),
        intents.filter {
            !AccountIntent.Init::class.java.isInstance(it)
        }
    )
}