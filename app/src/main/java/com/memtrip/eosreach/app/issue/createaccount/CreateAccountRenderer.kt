package com.memtrip.eosreach.app.issue.createaccount

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class CreateAccountRenderAction : MxRenderAction {
    object Idle : CreateAccountRenderAction()
    object OnProgress : CreateAccountRenderAction()
    object OnSuccess : CreateAccountRenderAction()
    object OnError : CreateAccountRenderAction()
}

interface CreateAccountViewLayout : MxViewLayout {
    fun showProgress()
    fun success()
    fun showError()
}

class CreateAccountViewRenderer @Inject internal constructor() : MxViewRenderer<CreateAccountViewLayout, CreateAccountViewState> {
    override fun layout(layout: CreateAccountViewLayout, state: CreateAccountViewState) = when (state.view) {
        CreateAccountViewState.View.Idle -> { }
        CreateAccountViewState.View.OnProgress -> {
            layout.showProgress()
        }
        CreateAccountViewState.View.OnSuccess -> {
            layout.success()
        }
        CreateAccountViewState.View.OnError -> {
            layout.showError()
        }
    }
}