package com.memtrip.eosreach.app.settings

import com.memtrip.mxandroid.MxViewState

data class SettingsViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        data class Populate(val exchangeRateCurrency: String) : View()
        object NavigateToCurrencyPairing : View()
        object NavigateToEosEndpoint : View()
        object NavigateToViewConfirmedTransactions : View()
        object NavigateToPrivateKeys : View()
        object NavigateToTelegram : View()
        object ConfirmClearData : View()
        object NavigateToEntry : View()
        object NavigateToAuthor : View()
    }
}