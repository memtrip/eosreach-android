package com.memtrip.eosreach.app.welcome.keyimported

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class KeyImportedRenderAction : MxRenderAction {
    object Idle : KeyImportedRenderAction()
    object Done : KeyImportedRenderAction()
}

interface KeyImportedViewLayout : MxViewLayout {
    fun done()
}

class KeyImportedViewRenderer @Inject internal constructor() : MxViewRenderer<KeyImportedViewLayout, KeyImportedViewState> {
    override fun layout(layout: KeyImportedViewLayout, state: KeyImportedViewState): Unit = when (state.view) {
        KeyImportedViewState.View.Idle -> { }
        KeyImportedViewState.View.Done -> {
            layout.done()
        }
    }
}