package com.memtrip.eosreach.app.issue.createaccount

import android.app.Application
import com.memtrip.eosreach.db.EosReachSharedPreferences
import com.memtrip.eosreach.wallet.EosKeyManager

import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class CreateAccountViewModel @Inject internal constructor(
    private val eosReachSharedPreferences: EosReachSharedPreferences,
    private val wallet: EosKeyManager,
    application: Application
) : MxViewModel<CreateAccountIntent, CreateAccountRenderAction, CreateAccountViewState>(
    CreateAccountViewState(view = CreateAccountViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: CreateAccountIntent): Observable<CreateAccountRenderAction> = when (intent) {
        is CreateAccountIntent.Init -> Observable.just(CreateAccountRenderAction.Idle)
        is CreateAccountIntent.CreateAccount -> saveAccount()
    }

    override fun reducer(previousState: CreateAccountViewState, renderAction: CreateAccountRenderAction): CreateAccountViewState = when (renderAction) {
        CreateAccountRenderAction.Idle -> previousState.copy(view = CreateAccountViewState.View.Idle)
        CreateAccountRenderAction.OnProgress -> previousState.copy(view = CreateAccountViewState.View.OnProgress)
        CreateAccountRenderAction.OnSuccess -> previousState.copy(view = CreateAccountViewState.View.OnSuccess)
        CreateAccountRenderAction.OnError -> previousState.copy(view = CreateAccountViewState.View.OnError)
    }

    override fun filterIntents(intents: Observable<CreateAccountIntent>): Observable<CreateAccountIntent> = Observable.merge(
        intents.ofType(CreateAccountIntent.Init::class.java).take(1),
        intents.filter {
            !CreateAccountIntent.Init::class.java.isInstance(it)
        }
    )

    private fun saveAccount(): Observable<CreateAccountRenderAction> {
        throw IllegalStateException("not implemented")
    }
}