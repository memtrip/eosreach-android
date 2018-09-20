package com.memtrip.eosreach.app.account.balance

import android.app.Application
import com.memtrip.eos.http.rpc.ChainApi
import com.memtrip.eos.http.rpc.model.contract.request.GetCurrencyBalance
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.accountforkey.AccountNameSystemBalance
import com.memtrip.eosreach.api.accountforkey.AccountsForPublicKeyRequestImpl
import com.memtrip.eosreach.api.customtokens.CustomTokensRequest
import com.memtrip.eosreach.api.customtokens.CustomTokensRequestImpl
import com.memtrip.eosreach.utils.RxSchedulers
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import retrofit2.Response
import javax.inject.Inject

class BalanceViewModel @Inject internal constructor(
    private val customTokensRequest: CustomTokensRequest,
    private val chainApi: ChainApi,
    private val rxSchedulers: RxSchedulers,
    application: Application
) : MxViewModel<BalanceIntent, BalanceRenderAction, BalanceViewState>(
    BalanceViewState(view = BalanceViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: BalanceIntent): Observable<BalanceRenderAction> = when (intent) {
        is BalanceIntent.Init -> Observable.just(BalanceRenderAction.Populate(intent.accountBalances))
        BalanceIntent.Idle -> Observable.just(BalanceRenderAction.Idle)
        is BalanceIntent.ScanForAirdropTokens -> scanForAirdropTokens(intent.accountName)
        BalanceIntent.NavigateToCreateAccount -> Observable.just(BalanceRenderAction.NavigateToCreateAccount)
        is BalanceIntent.NavigateToActions -> Observable.just(BalanceRenderAction.NavigateToActions(intent.balance))
    }

    override fun reducer(previousState: BalanceViewState, renderAction: BalanceRenderAction): BalanceViewState = when (renderAction) {
        BalanceRenderAction.Idle -> previousState.copy(view = BalanceViewState.View.Idle)
        is BalanceRenderAction.Populate -> previousState.copy(
            view = BalanceViewState.View.Populate,
            accountBalances = renderAction.accountBalances)
        BalanceRenderAction.NavigateToCreateAccount -> previousState.copy(
            view = BalanceViewState.View.NavigateToCreateAccount)
        is BalanceRenderAction.NavigateToActions -> previousState.copy(
            view = BalanceViewState.View.NavigateToActions(renderAction.contractAccountBalance))
        is BalanceRenderAction.OnAirdropError -> previousState.copy(
            view = BalanceViewState.View.OnAirdropError(renderAction.message))
        BalanceRenderAction.OnAirdropProgress -> previousState.copy(
            view = BalanceViewState.View.OnAirdropProgress)
        BalanceRenderAction.OnAirdropSuccess -> previousState.copy(
            view = BalanceViewState.View.OnAirdropSuccess)
    }

    override fun filterIntents(intents: Observable<BalanceIntent>): Observable<BalanceIntent> = Observable.merge(
        intents.ofType(BalanceIntent.Init::class.java).take(1),
        intents.filter {
            !BalanceIntent.Init::class.java.isInstance(it)
        }
    )

    private fun scanForAirdropTokens(accountName: String): Observable<BalanceRenderAction> {
        return customTokensRequest.getCustomTokens().flatMap<BalanceRenderAction> { tokenParent ->
            Observable.fromIterable(tokenParent.tokens).flatMap { token ->
                Observable.zip(
                    Observable.just(accountName),
                    chainApi.getCurrencyBalance(GetCurrencyBalance(
                        token.customtoken,
                        accountName
                    )).observeOn(rxSchedulers.main()).subscribeOn(rxSchedulers.background()).toObservable(),
                    BiFunction<String, Response<List<String>>, AccountNameSystemBalance> { accountName, response ->
                        if (response.isSuccessful) {
                            val balance = response.body()!!
                            if (balance.isNotEmpty()) {
                                AccountNameSystemBalance(accountName, balance[0])
                            } else {
                                AccountNameSystemBalance(accountName)
                            }
                        } else {
                            throw AccountsForPublicKeyRequestImpl.InnerAccountFailed()
                        }
                    })
            }
            .toList()
            .map { accountSystemBalanceList ->
                val results = accountSystemBalanceList.filter { accountNameSystemBalance ->
                    accountNameSystemBalance != null
                }

                if (results.isNotEmpty()) {
                    insertAirdrops()
                } else {
                    BalanceRenderAction.OnAirdropError(context().getString(R.string.balance_tokens_no_airdrops))
                }
            }
        }.onErrorReturn { error ->
            if (error is CustomTokensRequestImpl.CouldNotFetchTokens) {
                BalanceRenderAction.OnAirdropError(context().getString(R.string.balance_tokens_no_customtokens))
            } else {
                BalanceRenderAction.OnAirdropError(context().getString(R.string.balance_tokens_airdrop_generic_error))
            }
        }.toObservable().startWith(BalanceRenderAction.OnAirdropProgress)
    }

    private fun insertAirdrops(): BalanceRenderAction {
        return BalanceRenderAction.OnAirdropSuccess
    }
}