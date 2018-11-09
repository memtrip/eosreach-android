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

import com.memtrip.eosreach.api.blockproducer.RegisteredBlockProducer
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class RegisteredBlockProducersRenderAction : MxRenderAction {
    object Idle : RegisteredBlockProducersRenderAction()
    object OnProgress : RegisteredBlockProducersRenderAction()
    object Empty : RegisteredBlockProducersRenderAction()
    object OnLoadMoreProgress : RegisteredBlockProducersRenderAction()
    object OnError : RegisteredBlockProducersRenderAction()
    object OnLoadMoreError : RegisteredBlockProducersRenderAction()
    data class OnSuccess(val registeredBlockProducers: List<RegisteredBlockProducer>) : RegisteredBlockProducersRenderAction()
    data class WebsiteSelected(val url: String) : RegisteredBlockProducersRenderAction()
    data class RegisteredBlockProducersSelected(val accountName: String) : RegisteredBlockProducersRenderAction()
}

interface RegisteredBlockProducersViewLayout : MxViewLayout {
    fun showProgress()
    fun showEmpty()
    fun showError()
    fun showLoadMoreProgress()
    fun showLoadMoreError()
    fun populate(registeredBlockProducers: List<RegisteredBlockProducer>)
    fun websiteSelected(url: String)
    fun invalidWebsite(url: String)
    fun selectBlockProducer(accountName: String)
}

class RegisteredBlockProducersViewRenderer @Inject internal constructor() : MxViewRenderer<RegisteredBlockProducersViewLayout, RegisteredBlockProducersViewState> {
    override fun layout(layout: RegisteredBlockProducersViewLayout, state: RegisteredBlockProducersViewState): Unit = when (state.view) {
        RegisteredBlockProducersViewState.View.Idle -> {
        }
        RegisteredBlockProducersViewState.View.OnProgress -> {
            layout.showProgress()
        }
        RegisteredBlockProducersViewState.View.Empty -> {
            layout.showEmpty()
        }
        RegisteredBlockProducersViewState.View.OnLoadMoreProgress -> {
            layout.showLoadMoreProgress()
        }
        RegisteredBlockProducersViewState.View.OnLoadMoreError -> {
            layout.showLoadMoreError()
        }
        RegisteredBlockProducersViewState.View.OnError -> {
            layout.showError()
        }
        is RegisteredBlockProducersViewState.View.OnSuccess -> {
            layout.populate(state.view.registeredBlockProducers)
        }
        is RegisteredBlockProducersViewState.View.WebsiteSelected -> {
            layout.websiteSelected(state.view.website)
        }
        is RegisteredBlockProducersViewState.View.InvalidUrl -> {
            layout.invalidWebsite(state.view.website)
        }
        is RegisteredBlockProducersViewState.View.RegisteredBlockProducersSelected -> {
            layout.selectBlockProducer(state.view.accountName)
        }
    }
}