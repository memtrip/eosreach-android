package com.memtrip.eosreach.app.issue.createaccount

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class CreateAccountRenderAction : MxRenderAction {
    object Idle : CreateAccountRenderAction()
    object OnProgress : CreateAccountRenderAction()
    data class OnSuccess(val privateKey: String) : CreateAccountRenderAction()
    data class OnError(val error: String) : CreateAccountRenderAction()
    object Done : CreateAccountRenderAction()
}

interface CreateAccountViewLayout : MxViewLayout {
    fun showProgress()
    fun success(privateKey: String)
    fun showError(error: String)
    fun onSuccess(privateKey: String)
    fun done()
}

class CreateAccountViewRenderer @Inject internal constructor() : MxViewRenderer<CreateAccountViewLayout, CreateAccountViewState> {
    override fun layout(layout: CreateAccountViewLayout, state: CreateAccountViewState) = when (state.view) {
        CreateAccountViewState.View.Idle -> { }
        CreateAccountViewState.View.OnProgress -> {
            layout.showProgress()
        }
        is CreateAccountViewState.View.OnSuccess -> {
            layout.success(state.view.privateKey)
        }
        is CreateAccountViewState.View.OnError -> {
            layout.showError(state.view.error)
        }
        CreateAccountViewState.View.Done -> {
            layout.done()
        }
    }
}