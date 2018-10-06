package com.memtrip.eosreach.app.search

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