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
package com.memtrip.eosreach.app.settings.viewprivatekeys

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class ViewPrivateKeysRenderAction : MxRenderAction {
    object Idle : ViewPrivateKeysRenderAction()
    data class ShowPrivateKeys(val viewKeyPair: List<ViewKeyPair>) : ViewPrivateKeysRenderAction()
    object OnProgress : ViewPrivateKeysRenderAction()
    object NoPrivateKeys : ViewPrivateKeysRenderAction()
}

interface ViewPrivateKeysViewLayout : MxViewLayout {
    fun showPrivateKeys(viewKeyPair: List<ViewKeyPair>)
    fun showProgress()
    fun showNoPrivateKeys()
}

class ViewPrivateKeysViewRenderer @Inject internal constructor() : MxViewRenderer<ViewPrivateKeysViewLayout, ViewPrivateKeysViewState> {
    override fun layout(layout: ViewPrivateKeysViewLayout, state: ViewPrivateKeysViewState): Unit = when (state.view) {
        ViewPrivateKeysViewState.View.Idle -> {
        }
        is ViewPrivateKeysViewState.View.ShowPrivateKeys -> {
            layout.showPrivateKeys(state.view.viewKeyPair)
        }
        ViewPrivateKeysViewState.View.OnProgress -> {
            layout.showProgress()
        }
        ViewPrivateKeysViewState.View.NoPrivateKeys -> {
            layout.showNoPrivateKeys()
        }
    }
}