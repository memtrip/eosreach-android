package com.memtrip.eosreach.app.welcome.accountcreated

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class AccountCreatedViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<AccountCreatedIntent, AccountCreatedRenderAction, AccountCreatedViewState>(
    AccountCreatedViewState(view = AccountCreatedViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: AccountCreatedIntent): Observable<AccountCreatedRenderAction> = when (intent) {
        is AccountCreatedIntent.Init -> Observable.just(AccountCreatedRenderAction.OnProgress)
    }

    override fun reducer(previousState: AccountCreatedViewState, renderAction: AccountCreatedRenderAction) = when (renderAction) {
        AccountCreatedRenderAction.OnProgress -> previousState.copy(view = AccountCreatedViewState.View.OnProgress)
        AccountCreatedRenderAction.OnError -> previousState.copy(view = AccountCreatedViewState.View.OnError)
    }

    override fun filterIntents(intents: Observable<AccountCreatedIntent>): Observable<AccountCreatedIntent> = Observable.merge(
        intents.ofType(AccountCreatedIntent.Init::class.java).take(1),
        intents.filter {
            !AccountCreatedIntent.Init::class.java.isInstance(it)
        }
    )
}