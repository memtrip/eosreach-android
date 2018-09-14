package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.app.Application
import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.api.bandwidth.BandwidthRequest
import com.memtrip.eosreach.api.transfer.TransferError
import com.memtrip.eosreach.db.account.GetAccountByName
import com.memtrip.eosreach.wallet.EosKeyManager
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

abstract class BandwidthFormViewModel(
    private val bandWidthRequest: BandwidthRequest,
    private val getAccountByName: GetAccountByName,
    private val eosKeyManager: EosKeyManager,
    application: Application
) : MxViewModel<BandwidthFormIntent, BandwidthFormRenderAction, BandwidthFormViewState>(
    BandwidthFormViewState(view = BandwidthFormViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: BandwidthFormIntent): Observable<BandwidthFormRenderAction> = when (intent) {
        is BandwidthFormIntent.Init -> Observable.just(BandwidthFormRenderAction.Idle)
        BandwidthFormIntent.Idle -> Observable.just(BandwidthFormRenderAction.Idle)
        is BandwidthFormIntent.Commit -> commit(intent.bandwidthCommitType, intent.fromAccount, intent.netAmount, intent.cpuAmount)
    }

    override fun reducer(previousState: BandwidthFormViewState, renderAction: BandwidthFormRenderAction): BandwidthFormViewState = when (renderAction) {
        BandwidthFormRenderAction.Idle -> previousState.copy(
            view = BandwidthFormViewState.View.Idle)
        BandwidthFormRenderAction.OnProgress -> previousState.copy(
            view = BandwidthFormViewState.View.OnProgress)
        is BandwidthFormRenderAction.OnError -> previousState.copy(
            view = BandwidthFormViewState.View.OnError(renderAction.message, renderAction.log))
        is BandwidthFormRenderAction.OnSuccess -> previousState.copy(
            view = BandwidthFormViewState.View.OnSuccess(renderAction.transactionId))
    }

    override fun filterIntents(intents: Observable<BandwidthFormIntent>): Observable<BandwidthFormIntent> = Observable.merge(
        intents.ofType(BandwidthFormIntent.Init::class.java).take(1),
        intents.filter {
            !BandwidthFormIntent.Init::class.java.isInstance(it)
        }
    )

    private fun commit(
        bandwidthCommitType: BandwidthCommitType,
        fromAccount: String,
        netAmount: String,
        cpuAmount: String
    ): Observable<BandwidthFormRenderAction> {

        return getAccountByName.select(fromAccount).flatMap { accountEntity ->
            eosKeyManager.getPrivateKey(accountEntity.publicKey).flatMap { privateKey ->
                when (bandwidthCommitType) {
                    BandwidthCommitType.DELEGATE -> {
                        delegateBandwidth(
                            fromAccount,
                            netAmount,
                            cpuAmount,
                            privateKey)
                    }
                    BandwidthCommitType.UNDELEGATE -> {
                        unDelegateBandwidth(
                            fromAccount,
                            netAmount,
                            cpuAmount,
                            privateKey)
                    }
                }
            }.onErrorReturn {
                BandwidthFormRenderAction.OnError(
                    "error message",
                    it.message!!)
            }
        }.toObservable().startWith(BandwidthFormRenderAction.OnProgress)
    }

    private fun delegateBandwidth(
        fromAccount: String,
        netAmount: String,
        cpuAmount: String,
        privateKey: EosPrivateKey
    ) : Single<BandwidthFormRenderAction> {
        return bandWidthRequest.delegate(
            fromAccount,
            netAmount,
            cpuAmount,
            privateKey
        ).map { result ->
            if (result.success) {
                BandwidthFormRenderAction.OnSuccess(result.data!!.transactionId)
            } else {
                BandwidthFormRenderAction.OnError(
                    "error message",
                    result.apiError!!.body)
            }
        }
    }

    private fun unDelegateBandwidth(
        fromAccount: String,
        netAmount: String,
        cpuAmount: String,
        privateKey: EosPrivateKey
    ) : Single<BandwidthFormRenderAction> {
        return bandWidthRequest.unDelegate(
            fromAccount,
            netAmount,
            cpuAmount,
            privateKey
        ).map { result ->
            if (result.success) {
                BandwidthFormRenderAction.OnSuccess(result.data!!.transactionId)
            } else {
                BandwidthFormRenderAction.OnError(
                    "error message",
                    result.apiError!!.body)
            }
        }
    }
}