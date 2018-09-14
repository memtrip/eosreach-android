package com.memtrip.eosreach.app.account.resources.manage.manageram

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class RamFormRenderAction : MxRenderAction {
    object Idle : RamFormRenderAction()
    object OnProgress : RamFormRenderAction()
    data class OnError(val message: String, val log: String) : RamFormRenderAction()
    data class OnSuccess(val transactionId: String) : RamFormRenderAction()
}

interface RamFormViewLayout : MxViewLayout {
    fun showProgress()
    fun showError(message: String, log: String)
    fun showSuccess(transactionId: String)
}

class RamFormViewRenderer @Inject internal constructor() : MxViewRenderer<RamFormViewLayout, RamFormViewState> {
    override fun layout(layout: RamFormViewLayout, state: RamFormViewState): Unit = when (state.view) {
        RamFormViewState.View.Idle -> {
        }
        RamFormViewState.View.OnProgress -> {
            layout.showProgress()
        }
        is RamFormViewState.View.OnError -> {
            layout.showError(state.view.message, state.view.log)
        }
        is RamFormViewState.View.OnSuccess -> {
            layout.showSuccess(state.view.transactionId)
        }
    }
}