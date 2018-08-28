package com.memtrip.eosreach.app.welcome.splash

import com.memtrip.mxandroid.MxViewIntent

sealed class SplashIntent : MxViewIntent {
    object Init : SplashIntent()
    object NavigateToCreateAccount : SplashIntent()
    object NavigateToImportKeys : SplashIntent()
}