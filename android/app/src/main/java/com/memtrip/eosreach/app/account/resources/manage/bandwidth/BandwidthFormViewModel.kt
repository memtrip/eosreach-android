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
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.price.BalanceFormatter
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable

abstract class BandwidthFormViewModel(
    application: Application
) : MxViewModel<BandwidthFormIntent, BandwidthFormRenderAction, BandwidthFormViewState>(
    BandwidthFormViewState(view = BandwidthFormViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: BandwidthFormIntent): Observable<BandwidthFormRenderAction> = when (intent) {
        is BandwidthFormIntent.Init -> Observable.just(BandwidthFormRenderAction.Populate(
            intent.contractAccountBalance, intent.bandwidthFormBundle))
        BandwidthFormIntent.Idle -> Observable.just(BandwidthFormRenderAction.Idle)
        is BandwidthFormIntent.Confirm -> Observable.just(BandwidthFormRenderAction.NavigateToConfirm(
            intent.bandwidthCommitType,
            intent.toAccount,
            intent.netAmount,
            intent.cpuAmount,
            intent.transfer,
            intent.contractAccountBalance))
    }

    override fun reducer(previousState: BandwidthFormViewState, renderAction: BandwidthFormRenderAction): BandwidthFormViewState = when (renderAction) {
        BandwidthFormRenderAction.Idle -> previousState.copy(
            view = BandwidthFormViewState.View.Idle)
        is BandwidthFormRenderAction.Populate -> previousState.copy(
            view = BandwidthFormViewState.View.Populate(
                renderAction.contractAccountBalance,
                renderAction.bandwidthFormBundle))
        is BandwidthFormRenderAction.NavigateToConfirm -> previousState.copy(
            view = BandwidthFormViewState.View.NavigateToConfirm(createBandwidthBundle(
                renderAction.bandwidthCommitType,
                renderAction.toAccount,
                renderAction.netAmount,
                renderAction.cpuAmount,
                renderAction.transfer,
                renderAction.contractAccountBalance)))
    }

    override fun filterIntents(intents: Observable<BandwidthFormIntent>): Observable<BandwidthFormIntent> = Observable.merge(
        intents.ofType(BandwidthFormIntent.Init::class.java).take(1),
        intents.filter {
            !BandwidthFormIntent.Init::class.java.isInstance(it)
        }
    )

    private fun createBandwidthBundle(
        bandwidthCommitType: BandwidthCommitType,
        toAccount: String,
        netAmount: String,
        cpuAmount: String,
        transfer: Boolean,
        contractAccountBalance: ContractAccountBalance
    ): BandwidthBundle {
        return BandwidthBundle(
            toAccount,
            bandwidthCommitType,
            BalanceFormatter.create(netAmount, contractAccountBalance.balance.symbol),
            BalanceFormatter.create(cpuAmount, contractAccountBalance.balance.symbol),
            transfer)
    }
}