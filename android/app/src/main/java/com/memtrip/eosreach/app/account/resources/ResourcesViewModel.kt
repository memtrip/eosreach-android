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
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class ResourcesViewModel @Inject internal constructor(
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
    }

    override fun filterIntents(intents: Observable<ResourcesIntent>): Observable<ResourcesIntent> = Observable.merge(
        intents.ofType(ResourcesIntent.Init::class.java).take(1),
        intents.filter {
            !ResourcesIntent.Init::class.java.isInstance(it)
        }
    )
}