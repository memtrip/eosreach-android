package com.memtrip.eosreach.app.welcome.keyimported

import com.memtrip.mxandroid.MxViewIntent

sealed class KeyImportedIntent : MxViewIntent {
    object Init : KeyImportedIntent()
    object Done : KeyImportedIntent()
}