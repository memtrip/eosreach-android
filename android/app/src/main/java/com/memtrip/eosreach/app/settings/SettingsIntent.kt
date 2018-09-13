package com.memtrip.eosreach.app.settings

import com.memtrip.mxandroid.MxViewIntent

sealed class SettingsIntent : MxViewIntent {
    object Idle : SettingsIntent()
    object Init : SettingsIntent()
    object NavigateToCurrencyPairing : SettingsIntent()
    object NavigateToEosEndpoint : SettingsIntent()
    object NavigateToPrivateKeys : SettingsIntent()
    object NavigateToViewConfirmedTransactions : SettingsIntent()
    object NavigateToTelegram : SettingsIntent()
    object RequestClearDataAndLogout : SettingsIntent()
    object ConfirmClearDataAndLogout : SettingsIntent()
    object NavigateToAuthor : SettingsIntent()
}