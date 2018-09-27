package com.memtrip.eosreach.app.account.balance

import android.app.Application
import com.memtrip.eos.http.rpc.ChainApi
import com.memtrip.eos.http.rpc.model.contract.request.GetCurrencyBalance
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.accountforkey.AccountsForPublicKeyRequestImpl
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.api.customtokens.CustomTokensRequest
import com.memtrip.eosreach.api.customtokens.CustomTokensRequestImpl
import com.memtrip.eosreach.api.eosprice.EosPrice
import com.memtrip.eosreach.app.price.BalanceFormatter
import com.memtrip.eosreach.db.airdrop.InsertBalances
import com.memtrip.eosreach.utils.RxSchedulers
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import retrofit2.Response
import javax.inject.Inject

class BalanceViewModel @Inject internal constructor(
    private val customTokensRequest: CustomTokensRequest,
    private val insertBalances: InsertBalances,
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
        is BalanceRenderAction.OnAirdropSuccess -> previousState.copy(
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
                    BiFunction<String, Response<List<String>>, ContractAccountBalance> { accountName, response ->
                        if (response.isSuccessful) {
                            val balance = response.body()!!
                            if (balance.isNotEmpty()) {
                                ContractAccountBalance(
                                    token.customtoken,
                                    accountName,
                                    BalanceFormatter.deserialize(balance[0]),
                                    EosPrice.unavailable())
                            } else {
                                ContractAccountBalance.unavailable()
                            }
                        } else {
                            throw AccountsForPublicKeyRequestImpl.InnerAccountFailed()
                        }
                    })
            }
            .toList()
            .flatMap { contractAccountBalances ->
                val results = contractAccountBalances.filter { balance ->
                    balance.accountName != "unavailable" &&
                        balance.contractName != "unavailable"
                }

                if (results.isNotEmpty()) {
                    insertAirdrops(accountName, results)
                } else {
                    Single.just(BalanceRenderAction.OnAirdropError(context().getString(R.string.balance_tokens_no_airdrops)))
                }
            }
        }.onErrorReturn { error ->
            if (error is CustomTokensRequestImpl.NoAirdropsFound) {
                BalanceRenderAction.OnAirdropError(context().getString(R.string.balance_tokens_customtokens_empty))
            } else {
                BalanceRenderAction.OnAirdropError(context().getString(R.string.balance_tokens_airdrop_generic_error))
            }
        }.toObservable().startWith(BalanceRenderAction.OnAirdropProgress)
    }

    private fun insertAirdrops(accountName: String, balances: List<ContractAccountBalance>): Single<BalanceRenderAction> {
        return insertBalances.insert(accountName, balances).map { _ ->
            BalanceRenderAction.OnAirdropSuccess
        }
    }
}