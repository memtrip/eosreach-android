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
package com.memtrip.eosreach.app.blockproducer

import com.memtrip.eosreach.api.blockproducer.BlockProducerDetails
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class BlockProducerListRenderAction : MxRenderAction {
    object Idle : BlockProducerListRenderAction()
    object OnProgress : BlockProducerListRenderAction()
    object OnError : BlockProducerListRenderAction()
    data class OnSuccess(val blockProducerList: List<BlockProducerDetails>) : BlockProducerListRenderAction()
    data class BlockProducerSelected(val blockProducer: BlockProducerDetails) : BlockProducerListRenderAction()
    data class BlockProducerInformationSelected(
        val blockProducerDetails: BlockProducerDetails
    ) : BlockProducerListRenderAction()
}

interface BlockProducerListViewLayout : MxViewLayout {
    fun showProgress()
    fun showError()
    fun populate(blockProducerList: List<BlockProducerDetails>)
    fun blockProducerSelected(blockProducerDetails: BlockProducerDetails)
    fun blockProducerInformationSelected(blockProducerDetails: BlockProducerDetails)
}

class BlockProducerListViewRenderer @Inject internal constructor() : MxViewRenderer<BlockProducerListViewLayout, BlockProducerListViewState> {
    override fun layout(layout: BlockProducerListViewLayout, state: BlockProducerListViewState): Unit = when (state.view) {
        BlockProducerListViewState.View.Idle -> {
        }
        BlockProducerListViewState.View.OnProgress -> {
            layout.showProgress()
        }
        BlockProducerListViewState.View.OnError -> {
            layout.showError()
        }
        is BlockProducerListViewState.View.OnSuccess -> {
            layout.populate(state.view.blockProducers)
        }
        is BlockProducerListViewState.View.BlockProducerSelected -> {
            layout.blockProducerSelected(state.view.blockProducer)
        }
        is BlockProducerListViewState.View.BlockProducerInformationSelected -> {
            layout.blockProducerInformationSelected(state.view.blockProducerDetails)
        }
    }
}