package com.memtrip.eosreach.app.welcome.entry

import android.app.Application
import com.memtrip.eosreach.db.CountAccounts

import com.memtrip.eosreach.db.GetAccounts
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class EntryViewModel @Inject internal constructor(
    private val countAccounts: CountAccounts,
    private val getAccounts: GetAccounts,
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
        AccountListRenderAction.NavigateToAccount -> previousState.copy(view = EntryViewState.View.NavigateToAccount)
        AccountListRenderAction.OnError -> previousState.copy(view = EntryViewState.View.OnError)
    }

    private fun hasAccounts(): Observable<AccountListRenderAction> {
        return countAccounts.count()
            .flatMap<AccountListRenderAction> { count ->
                if (count > 0) {
                    getAccounts.select().map {
                        AccountListRenderAction.NavigateToAccount
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