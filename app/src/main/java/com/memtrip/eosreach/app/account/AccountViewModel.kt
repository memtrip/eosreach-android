package com.memtrip.eosreach.app.account

import android.app.Application
import com.memtrip.eosreach.api.account.EosAccountRequest
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
        is AccountIntent.Init -> getAccount()
    }

    override fun reducer(previousState: AccountViewState, renderAction: AccountRenderAction): AccountViewState = when (renderAction) {
        AccountRenderAction.OnProgress -> previousState.copy(view = AccountViewState.View.OnProgress)
        is AccountRenderAction.OnSuccess -> previousState.copy(
            view = AccountViewState.View.OnSuccess(
                renderAction.eosAccount,
                renderAction.accountBalances
            )
        )
        AccountRenderAction.OnError -> previousState.copy(view = AccountViewState.View.OnError)
    }

    override fun filterIntents(intents: Observable<AccountIntent>): Observable<AccountIntent> = Observable.merge(
        intents.ofType(AccountIntent.Init::class.java).take(1),
        intents.filter {
            !AccountIntent.Init::class.java.isInstance(it)
        }
    )

    private fun getAccount(): Observable<AccountRenderAction> {
        return accountUseCase.getAccount("eosio.token").map {
            if (it.success) {
                AccountRenderAction.OnSuccess(it.eosAccount!!, it.balances!!)
            } else {
                AccountRenderAction.OnError
            }
        }.toObservable()
    }
}