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
package com.memtrip.eosreach.app.explore

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class ExploreViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<ExploreIntent, ExploreRenderAction, ExploreViewState>(
    ExploreViewState(view = ExploreViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: ExploreIntent): Observable<ExploreRenderAction> = when (intent) {
        is ExploreIntent.Init -> Observable.just(ExploreRenderAction.Populate)
        ExploreIntent.SearchTabIdle -> Observable.just(ExploreRenderAction.SearchTabIdle)
        ExploreIntent.BlockProducerTabIdle -> Observable.just(ExploreRenderAction.BlockProducersTabIdle)
    }

    override fun reducer(previousState: ExploreViewState, renderAction: ExploreRenderAction): ExploreViewState = when (renderAction) {
        ExploreRenderAction.SearchTabIdle -> previousState.copy(
            view = ExploreViewState.View.Idle,
            page = ExploreFragmentPagerFragment.Page.SEARCH)
        ExploreRenderAction.BlockProducersTabIdle -> previousState.copy(
            view = ExploreViewState.View.Idle,
            page = ExploreFragmentPagerFragment.Page.SEARCH)
        is ExploreRenderAction.Populate -> previousState.copy(
            view = ExploreViewState.View.Populate)
    }

    override fun filterIntents(intents: Observable<ExploreIntent>): Observable<ExploreIntent> = Observable.merge(
        intents.ofType(ExploreIntent.Init::class.java).take(1),
        intents.filter {
            !ExploreIntent.Init::class.java.isInstance(it)
        }
    )
}