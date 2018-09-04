package com.memtrip.eosreach.app.price.currencypairing

import com.memtrip.mxandroid.MxViewIntent

sealed class CurrencyPairingIntent : MxViewIntent {
    object Init : CurrencyPairingIntent()
    data class CurrencyPair(val currencyPair: String) : CurrencyPairingIntent()
}