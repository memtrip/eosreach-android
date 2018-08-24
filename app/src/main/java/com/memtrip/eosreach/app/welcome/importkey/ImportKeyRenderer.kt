package com.memtrip.eosreach.app.welcome.importkey

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class ImportKeyRenderAction : MxRenderAction {
    object OnProgress : ImportKeyRenderAction()
    object OnError : ImportKeyRenderAction()
}

interface ImportKeyViewLayout : MxViewLayout {
    fun showProgress()
    fun showError()
}

class ImportKeyViewRenderer @Inject internal constructor() : MxViewRenderer<ImportKeyViewLayout, ImportKeyViewState> {
    override fun layout(layout: ImportKeyViewLayout, state: ImportKeyViewState) = when (state.view) {
        ImportKeyViewState.View.Idle -> {

        }
        ImportKeyViewState.View.OnProgress -> {
            layout.showProgress()
        }
        ImportKeyViewState.View.OnError -> {
            layout.showError()
        }
    }
}