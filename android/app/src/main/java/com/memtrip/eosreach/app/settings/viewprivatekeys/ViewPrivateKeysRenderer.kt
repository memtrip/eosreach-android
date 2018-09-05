package com.memtrip.eosreach.app.settings.viewprivatekeys

import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class ViewPrivateKeysRenderAction : MxRenderAction {
    object Idle : ViewPrivateKeysRenderAction()
    data class ShowPrivateKeys(val privateKeys: List<EosPrivateKey>) : ViewPrivateKeysRenderAction()
    object OnProgress : ViewPrivateKeysRenderAction()
    object NoPrivateKeys : ViewPrivateKeysRenderAction()
}

interface ViewPrivateKeysViewLayout : MxViewLayout {
    fun showPrivateKeys(privateKeys: List<EosPrivateKey>)
    fun showProgress()
    fun showNoPrivateKeys()
}

class ViewPrivateKeysViewRenderer @Inject internal constructor() : MxViewRenderer<ViewPrivateKeysViewLayout, ViewPrivateKeysViewState> {
    override fun layout(layout: ViewPrivateKeysViewLayout, state: ViewPrivateKeysViewState): Unit = when (state.view) {
        ViewPrivateKeysViewState.View.Idle -> {
        }
        is ViewPrivateKeysViewState.View.ShowPrivateKeys -> {
            layout.showPrivateKeys(state.view.privateKeys)
        }
        ViewPrivateKeysViewState.View.OnProgress -> {
            layout.showProgress()
        }
        ViewPrivateKeysViewState.View.NoPrivateKeys -> {
            layout.showNoPrivateKeys()
        }
    }
}