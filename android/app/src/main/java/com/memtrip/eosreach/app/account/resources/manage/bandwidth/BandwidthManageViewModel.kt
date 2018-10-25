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
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class BandwidthManageViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<BandwidthManageIntent, BandwidthManageRenderAction, BandwidthManageViewState>(
    BandwidthManageViewState(
        view = BandwidthManageViewState.View.Idle,
        page = BandwidthManageFragmentPagerAdapter.Page.DELEGATE
    ),
    application
) {

    override fun dispatcher(intent: BandwidthManageIntent): Observable<BandwidthManageRenderAction> = when (intent) {
        is BandwidthManageIntent.Init -> Observable.just(BandwidthManageRenderAction.Init)
        BandwidthManageIntent.DelegateBandwidthTabIdle -> Observable.just(BandwidthManageRenderAction.DelegateBandwidthTabIdle)
        BandwidthManageIntent.UnDelegateBandwidthTabIdle -> Observable.just(BandwidthManageRenderAction.UnDelegateBandwidthTabIdle)
        BandwidthManageIntent.AllocatedTabIdle -> Observable.just(BandwidthManageRenderAction.AllocatedTabIdle)
    }

    override fun reducer(previousState: BandwidthManageViewState, renderAction: BandwidthManageRenderAction): BandwidthManageViewState = when (renderAction) {
        is BandwidthManageRenderAction.Init -> previousState.copy(
            view = BandwidthManageViewState.View.Populate)
        BandwidthManageRenderAction.DelegateBandwidthTabIdle -> previousState.copy(
            view = BandwidthManageViewState.View.Idle,
            page = BandwidthManageFragmentPagerAdapter.Page.DELEGATE)
        BandwidthManageRenderAction.UnDelegateBandwidthTabIdle -> previousState.copy(
            view = BandwidthManageViewState.View.Idle,
            page = BandwidthManageFragmentPagerAdapter.Page.UNDELEGATE)
        BandwidthManageRenderAction.AllocatedTabIdle -> previousState.copy(
            view = BandwidthManageViewState.View.Idle,
            page = BandwidthManageFragmentPagerAdapter.Page.ALLOCATED)
    }

    override fun filterIntents(intents: Observable<BandwidthManageIntent>): Observable<BandwidthManageIntent> = Observable.merge(
        intents.ofType(BandwidthManageIntent.Init::class.java).take(1),
        intents.filter {
            !BandwidthManageIntent.Init::class.java.isInstance(it)
        }
    )
}