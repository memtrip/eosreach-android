package com.memtrip.eosreach.app.search

import android.app.Application
import com.memtrip.eosreach.api.account.EosAccountRequest
import com.memtrip.eosreach.db.account.AccountEntity
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class SearchViewModel @Inject internal constructor(
    private val eosAccountRequest: EosAccountRequest,
    application: Application
) : MxViewModel<SearchIntent, SearchRenderAction, SearchViewState>(
    SearchViewState(view = SearchViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: SearchIntent): Observable<SearchRenderAction> = when (intent) {
        is SearchIntent.Init -> Observable.just(SearchRenderAction.Idle)
        SearchIntent.Idle -> Observable.just(SearchRenderAction.Idle)
        is SearchIntent.SearchValueChanged -> searchValueChanged(intent.searchValue)
        is SearchIntent.ViewAccount -> Observable.just(SearchRenderAction.ViewAccount(intent.accountEntity))
    }

    override fun reducer(previousState: SearchViewState, renderAction: SearchRenderAction): SearchViewState = when (renderAction) {
        SearchRenderAction.OnProgress -> previousState.copy(view = SearchViewState.View.OnProgress)
        SearchRenderAction.Idle -> previousState.copy(view = SearchViewState.View.Idle)
        is SearchRenderAction.OnError -> previousState.copy(view = SearchViewState.View.OnError)
        is SearchRenderAction.OnSuccess -> previousState.copy(view = SearchViewState.View.OnSuccess(renderAction.accountEntity))
        is SearchRenderAction.ViewAccount -> previousState.copy(view = SearchViewState.View.ViewAccount(renderAction.accountEntity))
    }

    override fun filterIntents(intents: Observable<SearchIntent>): Observable<SearchIntent> = Observable.merge(
        intents.ofType(SearchIntent.Init::class.java).take(1),
        intents.filter {
            !SearchIntent.Init::class.java.isInstance(it)
        }
    )

    private fun searchValueChanged(value: String): Observable<SearchRenderAction> {
        return if (value.length == 12) {
            eosAccountRequest.getAccount(value).map { result ->
                if (result.success) {
                    val eosAccount = result.data!!
                        SearchRenderAction.OnSuccess(AccountEntity(
                            "",
                            eosAccount.accountName,
                            eosAccount.balance.amount,
                            eosAccount.balance.symbol
                        ))
                } else {
                    SearchRenderAction.OnError
                }
            }.toObservable().startWith(SearchRenderAction.OnProgress)
        } else {
            Observable.just(SearchRenderAction.Idle)
        }
    }
}