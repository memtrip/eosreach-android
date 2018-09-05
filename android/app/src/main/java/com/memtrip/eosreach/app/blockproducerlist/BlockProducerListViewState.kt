package com.memtrip.eosreach.app.blockproducerlist

import com.memtrip.eosreach.db.blockproducer.BlockProducerEntity
import com.memtrip.mxandroid.MxViewState

data class BlockProducerListViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        object OnError : View()
        data class OnSuccess(val blockProducers: List<BlockProducerEntity>) : View()
        data class BlockProducerSelected(val blockProducer: BlockProducerEntity) : View()
    }
}