package com.memtrip.eosreach.app.blockproducerlist

import com.memtrip.eosreach.db.blockproducer.BlockProducerEntity
import com.memtrip.mxandroid.MxViewIntent

sealed class BlockProducerListIntent : MxViewIntent {
    object Init : BlockProducerListIntent()
    object Retry : BlockProducerListIntent()
    data class BlockProducerSelected(
        val blockProducerEntity: BlockProducerEntity
    ) : BlockProducerListIntent()
}