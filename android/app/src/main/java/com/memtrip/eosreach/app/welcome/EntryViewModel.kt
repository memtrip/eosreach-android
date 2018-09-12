package com.memtrip.eosreach.app.welcome

import android.app.Application
import com.memtrip.eosreach.db.sharedpreferences.SelectedAccount
import com.memtrip.eosreach.db.account.CountAccounts
import com.memtrip.eosreach.db.account.GetAccountByName

import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class EntryViewModel @Inject internal constructor(
    private val countAccounts: CountAccounts,
    private val getAccountByName: GetAccountByName,
    private val selectedAccount: SelectedAccount,
    application: Application
) : MxViewModel<EntryIntent, AccountListRenderAction, EntryViewState>(
    EntryViewState(view = EntryViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: EntryIntent): Observable<AccountListRenderAction> = when (intent) {
        is EntryIntent.Init -> hasAccounts()
    }

    override fun reducer(previousState: EntryViewState, renderAction: AccountListRenderAction): EntryViewState = when (renderAction) {
        AccountListRenderAction.OnProgress -> previousState.copy(view = EntryViewState.View.Idle)
        AccountListRenderAction.NavigateToSplash -> previousState.copy(view = EntryViewState.View.NavigateToSplash)
        AccountListRenderAction.NavigateToAccountList -> previousState.copy(view = EntryViewState.View.NavigateToAccountList)
        is AccountListRenderAction.NavigateToAccount -> previousState.copy(view = EntryViewState.View.NavigateToAccount(renderAction.accountEntity))
        AccountListRenderAction.OnError -> previousState.copy(view = EntryViewState.View.OnError)
    }

    private fun hasAccounts(): Observable<AccountListRenderAction> {
        return countAccounts.count()
            .flatMap<AccountListRenderAction> { count ->
                if (count > 0) {
                    if (selectedAccount.exists()) {
                        getAccountByName.select(selectedAccount.get())
                            .map {
                                AccountListRenderAction.NavigateToAccount(it)
                            }
                    } else {
                        Single.just(AccountListRenderAction.NavigateToAccountList)
                    }
                } else {
                    Single.just(AccountListRenderAction.NavigateToSplash)
                }
            }
            .onErrorReturn { AccountListRenderAction.OnError }
            .toObservable()
            .startWith(AccountListRenderAction.OnProgress)
    }
}