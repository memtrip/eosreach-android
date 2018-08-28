package com.memtrip.eosreach.app.welcome.importkey

import com.memtrip.mxandroid.MxViewIntent

sealed class ImportKeyIntent : MxViewIntent {
    object Init : ImportKeyIntent()
    data class ImportKey(val privateKey: String) : ImportKeyIntent()
}