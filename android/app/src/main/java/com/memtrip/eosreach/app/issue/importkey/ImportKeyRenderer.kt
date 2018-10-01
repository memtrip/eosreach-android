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
package com.memtrip.eosreach.app.issue.importkey

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class ImportKeyRenderAction : MxRenderAction {
    object Idle : ImportKeyRenderAction()
    object OnProgress : ImportKeyRenderAction()
    object OnSuccess : ImportKeyRenderAction()
    data class OnError(val error: String) : ImportKeyRenderAction()
    object NavigateToGithubSource : ImportKeyRenderAction()
    object NavigateToSettings : ImportKeyRenderAction()
}

interface ImportKeyViewLayout : MxViewLayout {
    fun showDefaults()
    fun showProgress()
    fun showError(error: String)
    fun success()
    fun navigateToGithubSource()
    fun navigateToSettings()
}

class ImportKeyViewRenderer @Inject internal constructor() : MxViewRenderer<ImportKeyViewLayout, ImportKeyViewState> {
    override fun layout(layout: ImportKeyViewLayout, state: ImportKeyViewState): Unit = when (state.view) {
        ImportKeyViewState.View.Idle -> {
            layout.showDefaults()
        }
        ImportKeyViewState.View.OnProgress -> {
            layout.showProgress()
        }
        ImportKeyViewState.View.OnSuccess -> {
            layout.success()
        }
        is ImportKeyViewState.View.OnError -> {
            layout.showError(state.view.error)
        }
        ImportKeyViewState.View.NavigateToGithubSource -> {
            layout.navigateToGithubSource()
        }
        ImportKeyViewState.View.NavigateToSettings -> {
            layout.navigateToSettings()
        }
    }
}