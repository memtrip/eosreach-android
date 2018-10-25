/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.app.Application
import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.api.bandwidth.BandwidthError
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
        is BandwidthConfirmIntent.Commit -> commit(intent.bandwidthBundle, intent.contractAccountBalance)
    }

    override fun reducer(previousState: BandwidthConfirmViewState, renderAction: BandwidthConfirmRenderAction): BandwidthConfirmViewState = when (renderAction) {
        BandwidthConfirmRenderAction.Idle -> previousState.copy(
            view = BandwidthConfirmViewState.View.Idle)
        BandwidthConfirmRenderAction.OnProgress -> previousState.copy(
            view = BandwidthConfirmViewState.View.OnProgress)
        is BandwidthConfirmRenderAction.Populate -> previousState.copy(
            view = BandwidthConfirmViewState.View.Populate(renderAction.bandwidthBundle))
        is BandwidthConfirmRenderAction.OnGenericError -> previousState.copy(
            view = BandwidthConfirmViewState.View.OnGenericError(renderAction.message))
        is BandwidthConfirmRenderAction.OnTransactionError -> previousState.copy(
            view = BandwidthConfirmViewState.View.OnTransactionError(renderAction.message, renderAction.log))
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
        bandwidthBundle: BandwidthBundle,
        contractAccountBalance: ContractAccountBalance
    ): Observable<BandwidthConfirmRenderAction> {

        return getAccountByName.select(contractAccountBalance.accountName).flatMap { accountEntity ->
            eosKeyManager.getPrivateKey(accountEntity.publicKey).flatMap { privateKey ->
                when (bandwidthBundle.bandwidthCommitType) {
                    BandwidthCommitType.DELEGATE -> {
                        delegateBandwidth(
                            contractAccountBalance.accountName,
                            bandwidthBundle.targetAccount,
                            bandwidthBundle.netAmount,
                            bandwidthBundle.cpuAmount,
                            bandwidthBundle.transfer,
                            privateKey)
                    }
                    BandwidthCommitType.UNDELEGATE -> {
                        unDelegateBandwidth(
                            contractAccountBalance.accountName,
                            bandwidthBundle.targetAccount,
                            bandwidthBundle.netAmount,
                            bandwidthBundle.cpuAmount,
                            privateKey)
                    }
                }
            }.onErrorReturn {
                BandwidthConfirmRenderAction.OnTransactionError(
                    context().getString(R.string.app_dialog_transaction_error_body),
                    it.message!!)
            }
        }.toObservable().startWith(BandwidthConfirmRenderAction.OnProgress)
    }

    private fun delegateBandwidth(
        fromAccount: String,
        toAccount: String,
        netAmount: Balance,
        cpuAmount: Balance,
        transfer: Boolean,
        privateKey: EosPrivateKey
    ): Single<BandwidthConfirmRenderAction> {
        return bandWidthRequest.delegate(
            fromAccount,
            toAccount,
            BalanceFormatter.formatEosBalance(netAmount),
            BalanceFormatter.formatEosBalance(cpuAmount),
            transfer,
            privateKey
        ).map { result ->
            if (result.success) {
                BandwidthConfirmRenderAction.OnSuccess(result.data!!.transactionId)
            } else {
                if (result.apiError is BandwidthError.TransactionError) {
                    BandwidthConfirmRenderAction.OnTransactionError(
                        context().getString(R.string.app_dialog_transaction_error_body),
                        result.apiError.body)
                } else {
                    BandwidthConfirmRenderAction.OnGenericError(
                        context().getString(R.string.app_dialog_generic_error_body))
                }
            }
        }
    }

    private fun unDelegateBandwidth(
        fromAccount: String,
        toAccount: String,
        netAmount: Balance,
        cpuAmount: Balance,
        privateKey: EosPrivateKey
    ): Single<BandwidthConfirmRenderAction> {
        return bandWidthRequest.unDelegate(
            fromAccount,
            toAccount,
            BalanceFormatter.formatEosBalance(netAmount),
            BalanceFormatter.formatEosBalance(cpuAmount),
            privateKey
        ).map { result ->
            if (result.success) {
                BandwidthConfirmRenderAction.OnSuccess(result.data!!.transactionId)
            } else {
                if (result.apiError is BandwidthError.TransactionError) {
                    BandwidthConfirmRenderAction.OnTransactionError(
                        context().getString(R.string.app_dialog_transaction_error_body),
                        result.apiError.body)
                } else {
                    BandwidthConfirmRenderAction.OnGenericError(
                        context().getString(R.string.app_dialog_generic_error_body))
                }
            }
        }
    }
}