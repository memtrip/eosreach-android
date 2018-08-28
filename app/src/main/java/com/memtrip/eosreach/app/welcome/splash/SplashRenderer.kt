package com.memtrip.eosreach.app.welcome.splash

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class SplashRenderAction : MxRenderAction {
    object Idle : SplashRenderAction()
    object NavigateToCreateAccount : SplashRenderAction()
    object NavigateToImportKey : SplashRenderAction()
}

interface SplashViewLayout : MxViewLayout {
    fun navigateToCreateAccount()
    fun navigateToImportKey()
}

class SplashViewRenderer @Inject internal constructor() : MxViewRenderer<SplashViewLayout, SplashViewState> {
    override fun layout(layout: SplashViewLayout, state: SplashViewState) = when (state.view) {
        SplashViewState.View.Idle -> {
            print("ok")
        }
        SplashViewState.View.NavigateToCreateAccount -> {
            layout.navigateToCreateAccount()
        }
        SplashViewState.View.NavigateToImportKeys -> {
            layout.navigateToImportKey()
        }
    }
}