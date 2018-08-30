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
        AccountIntent.Init -> getAccount()
        AccountIntent.Retry -> getAccount()
    }

    override fun reducer(previousState: AccountViewState, renderAction: AccountRenderAction): AccountViewState = when (renderAction) {
        AccountRenderAction.OnProgress -> previousState.copy(view = AccountViewState.View.OnProgress)
        is AccountRenderAction.OnSuccess -> previousState.copy(
            view = AccountViewState.View.OnSuccess(
                renderAction.accountView
            )
        )
        is AccountRenderAction.OnErrorFetchingAccount -> previousState.copy(
            view = AccountViewState.View.OnErrorFetchingAccount(
                renderAction.accountName
            )
        )
        is AccountRenderAction.OnErrorFetchingBalances -> previousState.copy(
            view = AccountViewState.View.OnErrorFetchingAccount(
                renderAction.accountName
            )
        )
    }

    override fun filterIntents(intents: Observable<AccountIntent>): Observable<AccountIntent> = Observable.merge(
        intents.ofType(AccountIntent.Init::class.java).take(1),
        intents.filter {
            !AccountIntent.Init::class.java.isInstance(it)
        }
    )

    private fun getAccount(): Observable<AccountRenderAction> {
        return accountUseCase.getLatestAccount().flatMap { accountName ->
            accountUseCase.getAccountDetails("eosio.token", accountName).map {
                if (it.success) {
                    AccountRenderAction.OnSuccess(it)
                } else {
                    onError(accountName, it.error!!)
                }
            }
        }.toObservable().startWith(AccountRenderAction.OnProgress)
    }

    private fun onError(accountName: String, error: AccountView.Error): AccountRenderAction = when (error) {
        AccountView.Error.FetchingAccount -> AccountRenderAction.OnErrorFetchingAccount(accountName)
        AccountView.Error.FetchingBalances -> AccountRenderAction.OnErrorFetchingBalances(accountName)
    }
}