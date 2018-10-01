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
import com.memtrip.eosreach.api.ramprice.RamPriceRequest
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class ManageRamViewModel @Inject internal constructor(
    private val ramPriceRequest: RamPriceRequest,
    application: Application
) : MxViewModel<ManageRamIntent, ManageRamRenderAction, ManageRamViewState>(
    ManageRamViewState(
        view = ManageRamViewState.View.Idle,
        page = ManageRamFragmentPagerAdapter.Page.BUY
    ),
    application
) {

    override fun dispatcher(intent: ManageRamIntent): Observable<ManageRamRenderAction> = when (intent) {
        ManageRamIntent.BuyRamTabIdle -> Observable.just(ManageRamRenderAction.BuyRamTabIdle)
        ManageRamIntent.SellRamTabIdle -> Observable.just(ManageRamRenderAction.SellRamTabIdle)
        is ManageRamIntent.Init -> getRamPrice(intent.symbol)
    }

    override fun reducer(previousState: ManageRamViewState, renderAction: ManageRamRenderAction): ManageRamViewState = when (renderAction) {
        ManageRamRenderAction.BuyRamTabIdle -> previousState.copy(
            view = ManageRamViewState.View.Idle,
            page = ManageRamFragmentPagerAdapter.Page.BUY)
        ManageRamRenderAction.SellRamTabIdle -> previousState.copy(
            view = ManageRamViewState.View.Idle,
            page = ManageRamFragmentPagerAdapter.Page.SELL)
        ManageRamRenderAction.OnProgress -> previousState.copy(
            view = ManageRamViewState.View.OnProgress)
        ManageRamRenderAction.OnRamPriceError -> previousState.copy(
            view = ManageRamViewState.View.OnRamPriceError)
        is ManageRamRenderAction.Populate -> previousState.copy(
            view = ManageRamViewState.View.Populate(renderAction.ramPricePerKb))
    }

    override fun filterIntents(intents: Observable<ManageRamIntent>): Observable<ManageRamIntent> = Observable.merge(
        intents.ofType(ManageRamIntent.Init::class.java).take(1),
        intents.filter {
            !ManageRamIntent.Init::class.java.isInstance(it)
        }
    )

    private fun getRamPrice(symbol: String): Observable<ManageRamRenderAction> {
        return ramPriceRequest.getRamPrice(symbol).map<ManageRamRenderAction> { ramPrice ->
            ManageRamRenderAction.Populate(ramPrice)
        }.onErrorReturn {
            ManageRamRenderAction.OnRamPriceError
        }.toObservable().startWith(ManageRamRenderAction.OnProgress)
    }
}