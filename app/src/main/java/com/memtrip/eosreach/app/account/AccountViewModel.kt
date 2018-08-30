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
        AccountIntent.Idle -> Observable.just(AccountRenderAction.Idle)
        AccountIntent.RefreshAccounts -> Observable.just(AccountRenderAction.RefreshAccounts)
        AccountIntent.NavigateToImportKey -> Observable.just(AccountRenderAction.NavigateToImportKey)
        AccountIntent.NavigateToCreateAccount -> Observable.just(AccountRenderAction.NavigateToCreateAccount)
        AccountIntent.NavigateToSettings -> Observable.just(AccountRenderAction.NavigateToSettings)
    }

    override fun reducer(previousState: AccountViewState, renderAction: AccountRenderAction): AccountViewState = when (renderAction) {
        AccountRenderAction.Idle -> previousState.copy(view = AccountViewState.View.Idle)
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
        AccountRenderAction.RefreshAccounts -> previousState.copy(view = AccountViewState.View.Idle)
        AccountRenderAction.NavigateToImportKey -> previousState.copy(view = AccountViewState.View.NavigateToImportKey)
        AccountRenderAction.NavigateToCreateAccount -> previousState.copy(view = AccountViewState.View.NavigateToCreateAccount)
        AccountRenderAction.NavigateToSettings -> previousState.copy(view = AccountViewState.View.NavigateToSettings)
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