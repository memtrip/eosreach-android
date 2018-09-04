package com.memtrip.eosreach.app.price.currencypairing

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class CurrencyPairingRenderAction : MxRenderAction {
    object Idle : CurrencyPairingRenderAction()
    object OnProgress : CurrencyPairingRenderAction()
    data class OnError(val message: String) : CurrencyPairingRenderAction()
    object OnSuccess : CurrencyPairingRenderAction()
}

interface CurrencyPairingViewLayout : MxViewLayout {
    fun showProgress()
    fun showError(message: String)
    fun onSuccess()
}

class CurrencyPairingViewRenderer @Inject internal constructor() : MxViewRenderer<CurrencyPairingViewLayout, CurrencyPairingViewState> {
    override fun layout(layout: CurrencyPairingViewLayout, state: CurrencyPairingViewState): Unit = when (state.view) {
        CurrencyPairingViewState.View.Idle -> {
        }
        CurrencyPairingViewState.View.OnProgress -> {
            layout.showProgress()
        }
        is CurrencyPairingViewState.View.OnError -> {
            layout.showError(state.view.message)
        }
        CurrencyPairingViewState.View.OnSuccess -> {
            layout.onSuccess()
        }
    }
}