package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.app.Application
import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.eosreach.api.bandwidth.BandwidthRequest
import com.memtrip.eosreach.app.price.BalanceFormatter
import com.memtrip.eosreach.db.account.GetAccountByName
import com.memtrip.eosreach.wallet.EosKeyManager
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class BandwidthConfirmViewModel @Inject internal constructor(
    private val bandWidthRequest: BandwidthRequest,
    private val getAccountByName: GetAccountByName,
    private val eosKeyManager: EosKeyManager,
    application: Application
) : MxViewModel<BandwidthConfirmIntent, BandwidthConfirmRenderAction, BandwidthConfirmViewState>(
    BandwidthConfirmViewState(view = BandwidthConfirmViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: BandwidthConfirmIntent): Observable<BandwidthConfirmRenderAction> = when (intent) {
        is BandwidthConfirmIntent.Init -> Observable.just(BandwidthConfirmRenderAction.Populate(intent.bandwidthBundle))
        BandwidthConfirmIntent.Idle -> Observable.just(BandwidthConfirmRenderAction.Idle)
        is BandwidthConfirmIntent.Commit -> commit(intent.bandwidthBundle)
    }

    override fun reducer(previousState: BandwidthConfirmViewState, renderAction: BandwidthConfirmRenderAction): BandwidthConfirmViewState = when (renderAction) {
        BandwidthConfirmRenderAction.Idle -> previousState.copy(
            view = BandwidthConfirmViewState.View.Idle)
        BandwidthConfirmRenderAction.OnProgress -> previousState.copy(
            view = BandwidthConfirmViewState.View.OnProgress)
        is BandwidthConfirmRenderAction.Populate -> previousState.copy(
            view = BandwidthConfirmViewState.View.Populate(renderAction.bandwidthBundle))
        is BandwidthConfirmRenderAction.OnError -> previousState.copy(
            view = BandwidthConfirmViewState.View.OnError(renderAction.message, renderAction.log))
        is BandwidthConfirmRenderAction.OnSuccess -> previousState.copy(
            view = BandwidthConfirmViewState.View.OnSuccess(renderAction.transactionId))
    }

    override fun filterIntents(intents: Observable<BandwidthConfirmIntent>): Observable<BandwidthConfirmIntent> = Observable.merge(
        intents.ofType(BandwidthConfirmIntent.Init::class.java).take(1),
        intents.filter {
            !BandwidthConfirmIntent.Init::class.java.isInstance(it)
        }
    )

    private fun commit(
        bandwidthBundle: BandwidthBundle
    ): Observable<BandwidthConfirmRenderAction> {

        return getAccountByName.select(bandwidthBundle.fromAccount).flatMap { accountEntity ->
            eosKeyManager.getPrivateKey(accountEntity.publicKey).flatMap { privateKey ->
                when (bandwidthBundle.bandwidthCommitType) {
                    BandwidthCommitType.DELEGATE -> {
                        delegateBandwidth(
                            bandwidthBundle.fromAccount,
                            bandwidthBundle.netAmount,
                            bandwidthBundle.cpuAmount,
                            privateKey)
                    }
                    BandwidthCommitType.UNDELEGATE -> {
                        unDelegateBandwidth(
                            bandwidthBundle.fromAccount,
                            bandwidthBundle.netAmount,
                            bandwidthBundle.cpuAmount,
                            privateKey)
                    }
                }
            }.onErrorReturn {
                BandwidthConfirmRenderAction.OnError(
                    context().getString(R.string.app_dialog_transaction_error_body),
                    it.message!!)
            }
        }.toObservable().startWith(BandwidthConfirmRenderAction.OnProgress)
    }

    private fun delegateBandwidth(
        fromAccount: String,
        netAmount: Balance,
        cpuAmount: Balance,
        privateKey: EosPrivateKey
    ): Single<BandwidthConfirmRenderAction> {
        return bandWidthRequest.delegate(
            fromAccount,
            BalanceFormatter.formatEosBalance(netAmount),
            BalanceFormatter.formatEosBalance(cpuAmount),
            privateKey
        ).map { result ->
            if (result.success) {
                BandwidthConfirmRenderAction.OnSuccess(result.data!!.transactionId)
            } else {
                BandwidthConfirmRenderAction.OnError(
                    context().getString(R.string.app_dialog_transaction_error_body),
                    result.apiError!!.body)
            }
        }
    }

    private fun unDelegateBandwidth(
        fromAccount: String,
        netAmount: Balance,
        cpuAmount: Balance,
        privateKey: EosPrivateKey
    ): Single<BandwidthConfirmRenderAction> {
        return bandWidthRequest.unDelegate(
            fromAccount,
            BalanceFormatter.formatEosBalance(netAmount),
            BalanceFormatter.formatEosBalance(cpuAmount),
            privateKey
        ).map { result ->
            if (result.success) {
                BandwidthConfirmRenderAction.OnSuccess(result.data!!.transactionId)
            } else {
                BandwidthConfirmRenderAction.OnError(
                    context().getString(R.string.app_dialog_transaction_error_body),
                    result.apiError!!.body)
            }
        }
    }
}