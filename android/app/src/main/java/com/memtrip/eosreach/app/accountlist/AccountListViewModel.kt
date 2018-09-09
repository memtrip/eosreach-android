package com.memtrip.eosreach.app.accountlist

import android.app.Application
import com.memtrip.eosreach.db.SelectedAccount
import com.memtrip.eosreach.db.account.AccountEntity
import com.memtrip.eosreach.db.account.GetAccounts
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class AccountListViewModel @Inject internal constructor(
    private val getAccounts: GetAccounts,
    private val selectedAccount: SelectedAccount,
    private val accountListUseCase: AccountListUseCase,
    application: Application
) : MxViewModel<AccountListIntent, AccountListRenderAction, AccountListViewState>(
    AccountListViewState(view = AccountListViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: AccountListIntent): Observable<AccountListRenderAction> = when (intent) {
        is AccountListIntent.Init -> getAccounts()
        AccountListIntent.Idle -> Observable.just(AccountListRenderAction.Idle)
        is AccountListIntent.AccountSelected -> accountSelected(intent.accountName)
        AccountListIntent.RefreshAccounts -> refreshAccounts()
        AccountListIntent.NavigateToSettings -> Observable.just(AccountListRenderAction.NavigateToSettings)
    }

    override fun reducer(previousState: AccountListViewState, renderAction: AccountListRenderAction): AccountListViewState = when (renderAction) {
        AccountListRenderAction.Idle -> previousState.copy(
            view = AccountListViewState.View.Idle)
        AccountListRenderAction.OnProgress -> previousState.copy(
            view = AccountListViewState.View.OnProgress)
        is AccountListRenderAction.OnSuccess -> previousState.copy(
            view = AccountListViewState.View.OnSuccess(renderAction.accountList))
        AccountListRenderAction.OnError -> previousState.copy(
            view = AccountListViewState.View.OnError)
        is AccountListRenderAction.NavigateToAccount -> previousState.copy(
            view = AccountListViewState.View.NavigateToAccount(renderAction.accountEntity))
        AccountListRenderAction.NoAccounts -> previousState.copy(
            view = AccountListViewState.View.NoAccounts)
        AccountListRenderAction.NavigateToSettings -> previousState.copy(
            view = AccountListViewState.View.NavigateToSettings)
    }

    override fun filterIntents(intents: Observable<AccountListIntent>): Observable<AccountListIntent> = Observable.merge(
        intents.ofType(AccountListIntent.Init::class.java).take(1),
        intents.filter {
            !AccountListIntent.Init::class.java.isInstance(it)
        }
    )

    private fun getAccounts(): Observable<AccountListRenderAction> {
        return getAccounts.select().map<AccountListRenderAction> { accounts ->
            if (accounts.isEmpty()) {
                AccountListRenderAction.NoAccounts
            } else {
                AccountListRenderAction.OnSuccess(accounts)
            }
        }.toObservable()
    }

    private fun accountSelected(accountEntity: AccountEntity): Observable<AccountListRenderAction> {
        selectedAccount.put(accountEntity.accountName)
        return Observable.just(AccountListRenderAction.NavigateToAccount(accountEntity))
    }

    private fun refreshAccounts(): Observable<AccountListRenderAction> {
        selectedAccount.clear()
        return accountListUseCase
            .refreshAccounts()
            .toObservable()
            .flatMap { result ->
                if (result.success) {
                    getAccounts()
                } else {
                    refreshAccountsError(result.apiError!!)
                }
            }.onErrorReturn { AccountListRenderAction.OnError }
            .startWith(AccountListRenderAction.OnProgress)
    }

    private fun refreshAccountsError(error: AccountListUseCase.AccountsListError): Observable<AccountListRenderAction> = when(error) {
        AccountListUseCase.AccountsListError.RefreshAccountsFailed -> {
            Observable.just(AccountListRenderAction.OnError)
        }
        AccountListUseCase.AccountsListError.NoAccounts -> Observable.just(AccountListRenderAction.NoAccounts)
    }
}