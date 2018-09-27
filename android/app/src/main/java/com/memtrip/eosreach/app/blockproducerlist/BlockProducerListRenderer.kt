package com.memtrip.eosreach.app.blockproducerlist

import com.memtrip.eosreach.db.blockproducer.BlockProducerEntity
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class BlockProducerListRenderAction : MxRenderAction {
    object OnProgress : BlockProducerListRenderAction()
    object OnError : BlockProducerListRenderAction()
    data class OnSuccess(val blockProducerList: List<BlockProducerEntity>) : BlockProducerListRenderAction()
    data class BlockProducerSelected(val blockProducer: BlockProducerEntity) : BlockProducerListRenderAction()
}

interface BlockProducerListViewLayout : MxViewLayout {
    fun showProgress()
    fun showError()
    fun populate(blockProducerList: List<BlockProducerEntity>)
    fun blockProducerSelected(blockProducer: BlockProducerEntity)
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
    }
}