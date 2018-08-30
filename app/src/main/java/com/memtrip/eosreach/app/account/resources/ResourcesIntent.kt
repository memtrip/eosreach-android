package com.memtrip.eosreach.app.account.resources

import com.memtrip.mxandroid.MxViewIntent

sealed class ResourcesIntent : MxViewIntent {
    object Init : ResourcesIntent()
}