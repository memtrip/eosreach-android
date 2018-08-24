package com.memtrip.eosreach.app.welcome.splash

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class SplashRenderAction : MxRenderAction {
    object OnProgress : SplashRenderAction()
    object OnError : SplashRenderAction()
}

interface SplashViewLayout : MxViewLayout {
    fun showProgress()
    fun showError()
}

class SplashViewRenderer @Inject internal constructor() : MxViewRenderer<SplashViewLayout, SplashViewState> {
    override fun layout(layout: SplashViewLayout, state: SplashViewState) = when (state.view) {
        SplashViewState.View.Idle -> {

        }
        SplashViewState.View.OnProgress -> {
            layout.showProgress()
        }
        SplashViewState.View.OnError -> {
            layout.showError()
        }
    }
}