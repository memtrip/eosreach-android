/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
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
    object NavigateToViewConfirmedTransactions : SettingsRenderAction()
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
    fun navigateToViewConfirmedTransactionLog()
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
        SettingsViewState.View.NavigateToViewConfirmedTransactions -> {
            layout.navigateToViewConfirmedTransactionLog()
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