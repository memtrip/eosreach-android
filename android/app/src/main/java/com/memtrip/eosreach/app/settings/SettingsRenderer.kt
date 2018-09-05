package com.memtrip.eosreach.app.settings

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class SettingsRenderAction : MxRenderAction {
    object Idle : SettingsRenderAction()
    data class Populate(val exchangeRateCurrency: String) : SettingsRenderAction()
    object NavigateToCurrencyPairing : SettingsRenderAction()
    object NavigateToEosEndpoint : SettingsRenderAction()
    object NavigateToPrivateKeys : SettingsRenderAction()
    object NavigateToTelegram : SettingsRenderAction()
    object ConfirmClearData : SettingsRenderAction()
    object NavigateToEntry : SettingsRenderAction()
    object NavigateToAuthor : SettingsRenderAction()
}

interface SettingsViewLayout : MxViewLayout {
    fun populate(exchangeRateCurrency: String)
    fun navigateToCurrencyPairing()
    fun navigateToEosEndpoint()
    fun navigateToViewPrivateKeys()
    fun navigateToTelegramGroup()
    fun confirmClearData()
    fun navigateToEntry()
    fun navigateToAuthor()
}

class SettingsViewRenderer @Inject internal constructor() : MxViewRenderer<SettingsViewLayout, SettingsViewState> {
    override fun layout(layout: SettingsViewLayout, state: SettingsViewState): Unit = when (state.view) {
        SettingsViewState.View.Idle -> {
        }
        SettingsViewState.View.NavigateToCurrencyPairing -> {
            layout.navigateToCurrencyPairing()
        }
        SettingsViewState.View.NavigateToEosEndpoint -> {
            layout.navigateToEosEndpoint()
        }
        SettingsViewState.View.NavigateToPrivateKeys -> {
            layout.navigateToViewPrivateKeys()
        }
        SettingsViewState.View.NavigateToTelegram -> {
            layout.navigateToTelegramGroup()
        }
        SettingsViewState.View.ConfirmClearData -> {
            layout.confirmClearData()
        }
        SettingsViewState.View.NavigateToEntry -> {
            layout.navigateToEntry()
        }
        is SettingsViewState.View.Populate -> {
            layout.populate(state.view.exchangeRateCurrency)
        }
        SettingsViewState.View.NavigateToAuthor -> {
            layout.navigateToAuthor()
        }
    }
}