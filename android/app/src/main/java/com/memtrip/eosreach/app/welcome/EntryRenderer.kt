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
package com.memtrip.eosreach.app.welcome

import com.memtrip.eosreach.db.account.AccountEntity
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class EntryRenderAction : MxRenderAction {
    object OnProgress : EntryRenderAction()
    object OnError : EntryRenderAction()
    object OnRsaEncryptionFailed : EntryRenderAction()
    object NavigateToSplash : EntryRenderAction()
    data class NavigateToAccount(val accountEntity: AccountEntity) : EntryRenderAction()
}

interface EntryViewLayout : MxViewLayout {
    fun showProgress()
    fun navigateToSplash()
    fun showError()
    fun showRsaEncryptionFailed()
    fun navigateToAccount(accountEntity: AccountEntity)
}

class EntryViewRenderer @Inject internal constructor() : MxViewRenderer<EntryViewLayout, EntryViewState> {
    override fun layout(layout: EntryViewLayout, state: EntryViewState): Unit = when (state.view) {
        EntryViewState.View.Idle -> {
        }
        EntryViewState.View.NavigateToSplash -> {
            layout.navigateToSplash()
        }
        EntryViewState.View.OnProgress -> {
            layout.showProgress()
        }
        EntryViewState.View.OnError -> {
            layout.showError()
        }
        EntryViewState.View.OnRsaEncryptionFailed -> {
            layout.showRsaEncryptionFailed()
        }
        is EntryViewState.View.NavigateToAccount -> {
            layout.navigateToAccount(state.view.accountEntity)
        }
    }
}