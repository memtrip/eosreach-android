package com.memtrip.eosreach.app.welcome.newaccount

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class NewAccountRenderAction : MxRenderAction {
    object OnProgress : NewAccountRenderAction()
    object OnError : NewAccountRenderAction()
}

interface NewAccountViewLayout : MxViewLayout {
    fun showProgress()
    fun showError()
}

class NewAccountViewRenderer @Inject internal constructor() : MxViewRenderer<NewAccountViewLayout, NewAccountViewState> {
    override fun layout(layout: NewAccountViewLayout, state: NewAccountViewState) = when (state.view) {
        NewAccountViewState.View.Idle -> {

        }
        NewAccountViewState.View.OnProgress -> {
            layout.showProgress()
        }
        NewAccountViewState.View.OnError -> {
            layout.showError()
        }
    }
}