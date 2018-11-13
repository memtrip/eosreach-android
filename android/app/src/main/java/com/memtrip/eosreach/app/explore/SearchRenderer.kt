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
package com.memtrip.eosreach.app.explore

import com.memtrip.eosreach.db.account.AccountEntity
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class SearchRenderAction : MxRenderAction {
    object Idle : SearchRenderAction()
    object OnProgress : SearchRenderAction()
    object OnError : SearchRenderAction()
    data class OnSuccess(val accountEntity: AccountEntity) : SearchRenderAction()
    data class ViewAccount(val accountEntity: AccountEntity) : SearchRenderAction()
}

interface SearchViewLayout : MxViewLayout {
    fun showProgress()
    fun showError()
    fun searchResult(accountEntity: AccountEntity)
    fun viewAccount(accountEntity: AccountEntity)
}

class SearchViewRenderer @Inject internal constructor() : MxViewRenderer<SearchViewLayout, SearchViewState> {
    override fun layout(layout: SearchViewLayout, state: SearchViewState): Unit = when (state.view) {
        SearchViewState.View.Idle -> {
        }
        SearchViewState.View.OnProgress -> {
            layout.showProgress()
        }
        is SearchViewState.View.OnError -> {
            layout.showError()
        }
        is SearchViewState.View.OnSuccess -> {
            layout.searchResult(state.view.accountEntity)
        }
        is SearchViewState.View.ViewAccount -> {
            layout.viewAccount(state.view.accountEntity)
        }
    }
}