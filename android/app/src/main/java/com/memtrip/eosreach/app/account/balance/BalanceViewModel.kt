package com.memtrip.eosreach.app.account.balance

import android.app.Application
import com.memtrip.eos.http.rpc.ChainApi
import com.memtrip.eos.http.rpc.model.contract.request.GetCurrencyBalance
import com.memtrip.eosreach.api.accountforkey.AccountNameSystemBalance
import com.memtrip.eosreach.api.accountforkey.AccountsForPublicKeyRequestImpl
import com.memtrip.eosreach.api.customtokens.CustomTokensRequest
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import retrofit2.Response
import javax.inject.Inject

class BalanceViewModel @Inject internal constructor(
    private val customTokensRequest: CustomTokensRequest,
    private val chainApi: ChainApi,
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
            view = BalanceViewState.View.Populate(renderAction.accountBalances))
        BalanceRenderAction.NavigateToCreateAccount -> previousState.copy(
            view = BalanceViewState.View.NavigateToCreateAccount)
        is BalanceRenderAction.NavigateToActions -> previousState.copy(
            view = BalanceViewState.View.NavigateToActions(renderAction.contractAccountBalance))
        is BalanceRenderAction.OnAirdropError -> previousState.copy(
            view = BalanceViewState.View.OnAirdropError(renderAction.message))
        BalanceRenderAction.OnAirdropProgress -> previousState.copy(
            view = BalanceViewState.View.OnAirdropProgress)
    }

    override fun filterIntents(intents: Observable<BalanceIntent>): Observable<BalanceIntent> = Observable.merge(
        intents.ofType(BalanceIntent.Init::class.java).take(1),
        intents.filter {
            !BalanceIntent.Init::class.java.isInstance(it)
        }
    )

    /**
    Observable.zip(
        Observable.just(accountName),
        chainApi.getCurrencyBalance(GetCurrencyBalance(
            "eosio.token",
            accountName
        )).toObservable(),
        BiFunction<String, Response<List<String>>, AccountNameSystemBalance> { accountName, response ->
            if (response.isSuccessful) {
                    val balance = response.body()!!
                    if (balance.isNotEmpty()) {
                        AccountNameSystemBalance(accountName, balance[0])
                    } else {
                        AccountNameSystemBalance(accountName)
                    }
            } else {
                throw InnerAccountFailed()
            }
        })
     */
    private fun scanForAirdropTokens(accountName: String): Observable<BalanceRenderAction> {
        return customTokensRequest.getCustomTokens().flatMap<BalanceRenderAction> { tokenParent ->
            Observable.fromIterable(tokenParent.tokens).flatMap { token ->
                Observable.zip(
                    Observable.just(accountName),
                    chainApi.getCurrencyBalance(GetCurrencyBalance(
                        token.customtoken,
                        accountName
                    )).toObservable(),
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
                BalanceRenderAction.OnAirdropError("error")
            }
        }.onErrorReturn {
            BalanceRenderAction.OnAirdropError("error")
        }.toObservable().startWith(BalanceRenderAction.OnAirdropProgress)
    }
}