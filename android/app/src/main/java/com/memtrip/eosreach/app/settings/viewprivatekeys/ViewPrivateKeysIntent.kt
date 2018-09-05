package com.memtrip.eosreach.app.settings.viewprivatekeys

import com.memtrip.mxandroid.MxViewIntent

sealed class ViewPrivateKeysIntent : MxViewIntent {
    object Init : ViewPrivateKeysIntent()
    object DecryptPrivateKeys : ViewPrivateKeysIntent()
}