package com.memtrip.eosreach.app.blockproducer

import com.memtrip.eosreach.api.blockproducer.BlockProducerDetails
import com.memtrip.mxandroid.MxViewIntent

sealed class ViewBlockProducerIntent : MxViewIntent {
    data class InitWithDetails(val blockProducerDetails: BlockProducerDetails) : ViewBlockProducerIntent()
    data class InitWithName(val accountName: String) : ViewBlockProducerIntent()
    object Idle : ViewBlockProducerIntent()
    data class NavigateToUrl(val url: String) : ViewBlockProducerIntent()
}