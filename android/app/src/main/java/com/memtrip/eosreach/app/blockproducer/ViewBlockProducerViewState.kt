package com.memtrip.eosreach.app.blockproducer

import com.memtrip.eosreach.api.blockproducer.BlockProducerDetails
import com.memtrip.mxandroid.MxViewState
import com.memtrip.mxandroid.MxViewState.Companion.id

data class ViewBlockProducerViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        object OnError : View()
        data class OnInvalidUrl(val url: String, val unique: Int = id()) : View()
        data class NavigateToUrl(val url: String) : View()
        data class Populate(val blockProducerDetails: BlockProducerDetails) : View()
    }
}