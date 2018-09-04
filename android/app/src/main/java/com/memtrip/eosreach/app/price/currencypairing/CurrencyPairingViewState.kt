package com.memtrip.eosreach.app.price.currencypairing

import com.memtrip.mxandroid.MxViewState
import com.memtrip.mxandroid.MxViewState.Companion.id

data class CurrencyPairingViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        data class OnError(val message: String, val unique: Int = id()) : View()
        object OnSuccess : View()
    }
}