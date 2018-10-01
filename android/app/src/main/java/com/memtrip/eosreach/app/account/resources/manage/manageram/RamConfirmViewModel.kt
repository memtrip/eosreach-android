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
package com.memtrip.eosreach.app.account.resources.manage.manageram

import android.app.Application
import android.app.KeyguardManager
import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eosreach.api.ram.RamRequest
import com.memtrip.eosreach.db.account.GetAccountByName
import com.memtrip.eosreach.wallet.EosKeyManager
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class RamConfirmViewModel @Inject internal constructor(
    private val ramRequest: RamRequest,
    private val getAccountByName: GetAccountByName,
    private val eosKeyManager: EosKeyManager,
    application: Application
) : MxViewModel<RamConfirmIntent, RamConfirmRenderAction, RamConfirmViewState>(
    RamConfirmViewState(view = RamConfirmViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: RamConfirmIntent): Observable<RamConfirmRenderAction> = when (intent) {
        RamConfirmIntent.Idle -> Observable.just(RamConfirmRenderAction.Idle)
        is RamConfirmIntent.Init -> Observable.just(RamConfirmRenderAction.Populate)
        is RamConfirmIntent.Confirm -> confirm(intent.account, intent.kb, intent.commitType)
    }

    override fun reducer(previousState: RamConfirmViewState, renderAction: RamConfirmRenderAction): RamConfirmViewState = when (renderAction) {
        RamConfirmRenderAction.Idle -> previousState.copy(
            view = RamConfirmViewState.View.Idle)
        RamConfirmRenderAction.OnProgress -> previousState.copy(
            view = RamConfirmViewState.View.OnProgress)
        RamConfirmRenderAction.Populate -> previousState.copy(
            view = RamConfirmViewState.View.Populate)
        is RamConfirmRenderAction.OnSuccess -> previousState.copy(
            view = RamConfirmViewState.View.OnSuccess(renderAction.transferReceipt))
        is RamConfirmRenderAction.OnError -> previousState.copy(
            view = RamConfirmViewState.View.OnError(renderAction.log))
    }

    override fun filterIntents(intents: Observable<RamConfirmIntent>): Observable<RamConfirmIntent> = Observable.merge(
        intents.ofType(RamConfirmIntent.Init::class.java).take(1),
        intents.filter {
            !RamConfirmIntent.Init::class.java.isInstance(it)
        }
    )

    private fun confirm(
        account: String,
        kb: String,
        commitType: RamCommitType
    ): Observable<RamConfirmRenderAction> {
        return getAccountByName.select(account).flatMap { accountEntity ->
            eosKeyManager.getPrivateKey(accountEntity.publicKey).flatMap { privateKey ->
                if (commitType == RamCommitType.BUY) {
                    buyRam(account, kb.toDouble(), privateKey)
                } else {
                    sellRam(account, kb.toDouble(), privateKey)
                }
            }
        }.toObservable().startWith(RamConfirmRenderAction.OnProgress)
    }

    private fun buyRam(
        account: String,
        quantity: Double,
        privateKey: EosPrivateKey
    ): Single<RamConfirmRenderAction> {
        return ramRequest.buy(account, quantity, privateKey).flatMap { result ->
            if (result.success) {
                Single.just(RamConfirmRenderAction.OnSuccess(result.data!!))
            } else {
                Single.just(RamConfirmRenderAction.OnError(result.apiError!!.body))
            }
        }
    }

    private fun sellRam(
        account: String,
        quantity: Double,
        privateKey: EosPrivateKey
    ): Single<RamConfirmRenderAction> {
        return ramRequest.sell(account, quantity, privateKey).flatMap { result ->
            if (result.success) {
                Single.just(RamConfirmRenderAction.OnSuccess(result.data!!))
            } else {
                Single.just(RamConfirmRenderAction.OnError(result.apiError!!.body))
            }
        }
    }
}