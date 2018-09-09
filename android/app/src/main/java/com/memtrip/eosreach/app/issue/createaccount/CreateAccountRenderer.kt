package com.memtrip.eosreach.app.issue.createaccount

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class CreateAccountRenderAction : MxRenderAction {
    object Idle : CreateAccountRenderAction()
    object OnProgress : CreateAccountRenderAction()
    data class OnCreateAccountSuccess(val privateKey: String) : CreateAccountRenderAction()
    data class OnCreateAccountError(val error: String) : CreateAccountRenderAction()
    object OnImportKeyProgress : CreateAccountRenderAction()
    data class OnImportKeyError(val error: String) : CreateAccountRenderAction()
    object NavigateToAccountList : CreateAccountRenderAction()
}

interface CreateAccountViewLayout : MxViewLayout {
    fun showCreateAccountProgress()
    fun showAccountCreated(privateKey: String)
    fun showCreateAccountError(error: String)
    fun showImportKeyProgress()
    fun showImportKeyError(error: String)
    fun navigateToAccountList()
}

class CreateAccountViewRenderer @Inject internal constructor() : MxViewRenderer<CreateAccountViewLayout, CreateAccountViewState> {
    override fun layout(layout: CreateAccountViewLayout, state: CreateAccountViewState) = when (state.view) {
        CreateAccountViewState.View.Idle -> { }
        CreateAccountViewState.View.OnCreateAccountProgress -> {
            layout.showCreateAccountProgress()
        }
        is CreateAccountViewState.View.OnCreateAccountSuccess -> {
            layout.showAccountCreated(state.view.privateKey)
        }
        is CreateAccountViewState.View.CreateAccountError -> {
            layout.showCreateAccountError(state.view.error)
        }
        CreateAccountViewState.View.OnImportKeyProgress -> {
            layout.showImportKeyProgress()
        }
        is CreateAccountViewState.View.ImportKeyError -> {
            layout.showImportKeyError(state.view.error)
        }
        CreateAccountViewState.View.NavigateToAccountList -> {
            layout.navigateToAccountList()
        }
    }
}