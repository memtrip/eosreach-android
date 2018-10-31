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
package com.memtrip.eosreach.app.account.resources

import android.app.Application
import com.memtrip.eosreach.api.refund.BandwidthRefundError
import com.memtrip.eosreach.api.refund.BandwidthRefundRequest
import com.memtrip.eosreach.db.account.GetAccountByName
import com.memtrip.eosreach.wallet.EosKeyManager
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class ResourcesViewModel @Inject internal constructor(
    private val getAccountByName: GetAccountByName,
    private val bandwidthRefundRequest: BandwidthRefundRequest,
    private val eosKeyManager: EosKeyManager,
    application: Application
) : MxViewModel<ResourcesIntent, ResourcesRenderAction, ResourcesViewState>(
    ResourcesViewState(view = ResourcesViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: ResourcesIntent): Observable<ResourcesRenderAction> = when (intent) {
        is ResourcesIntent.Init -> Observable.just(ResourcesRenderAction.Populate(
            intent.eosAccount,
            intent.contractAccountBalance))
        ResourcesIntent.Idle -> Observable.just(ResourcesRenderAction.Idle)
        ResourcesIntent.NavigateToManageBandwidth -> Observable.just(ResourcesRenderAction.NavigateToManageBandwidth)
        ResourcesIntent.NavigateToManageRam -> Observable.just(ResourcesRenderAction.NavigateToManageRam)
        is ResourcesIntent.NavigateToManageBandwidthWithAccountName ->
            Observable.just(ResourcesRenderAction.NavigateToManageBandwidthWithAccountName)
        is ResourcesIntent.RequestRefund -> requestRefund(intent.accountName)
    }

    override fun reducer(previousState: ResourcesViewState, renderAction: ResourcesRenderAction): ResourcesViewState = when (renderAction) {
        is ResourcesRenderAction.Populate -> previousState.copy(
            view = ResourcesViewState.View.Populate(renderAction.eosAccount, renderAction.contractAccountBalance))
        ResourcesRenderAction.Idle -> previousState.copy(
            view = ResourcesViewState.View.Idle)
        ResourcesRenderAction.NavigateToManageBandwidth -> previousState.copy(
            view = ResourcesViewState.View.NavigateToManageBandwidth)
        ResourcesRenderAction.NavigateToManageRam -> previousState.copy(
            view = ResourcesViewState.View.NavigateToManageRam)
        is ResourcesRenderAction.NavigateToManageBandwidthWithAccountName -> previousState.copy(
            view = ResourcesViewState.View.NavigateToManageBandwidthWithAccountName)
        ResourcesRenderAction.RefundProgress -> previousState.copy(
            view = ResourcesViewState.View.RefundProgress)
        ResourcesRenderAction.RefundSuccess -> previousState.copy(
            view = ResourcesViewState.View.RefundSuccess)
        ResourcesRenderAction.RefundFailed -> previousState.copy(
            view = ResourcesViewState.View.RefundFailed)
        is ResourcesRenderAction.RefundFailedWithLog -> previousState.copy(
            view = ResourcesViewState.View.RefundFailedWithLog(renderAction.log))
    }

    override fun filterIntents(intents: Observable<ResourcesIntent>): Observable<ResourcesIntent> = Observable.merge(
        intents.ofType(ResourcesIntent.Init::class.java).take(1),
        intents.filter {
            !ResourcesIntent.Init::class.java.isInstance(it)
        }
    )

    private fun requestRefund(accountName: String): Observable<ResourcesRenderAction> {
        return getAccountByName.select(accountName).flatMap { accountEntity ->
            eosKeyManager.getPrivateKey(accountEntity.publicKey).flatMap { privateKey ->
                bandwidthRefundRequest.requestRefund(
                    accountName,
                    privateKey
                ).map { result ->
                    if (result.success) {
                        ResourcesRenderAction.RefundSuccess
                    } else {
                        bandwidthRefundError(result.apiError!!)
                    }
                }
            }
        }.toObservable().startWith(ResourcesRenderAction.RefundProgress)
    }

    private fun bandwidthRefundError(bandwidthRefundError: BandwidthRefundError): ResourcesRenderAction = when (bandwidthRefundError) {
        is BandwidthRefundError.TransactionError -> {
            ResourcesRenderAction.RefundFailedWithLog(bandwidthRefundError.body)
        }
        BandwidthRefundError.GenericError -> {
            ResourcesRenderAction.RefundFailed
        }
    }
}