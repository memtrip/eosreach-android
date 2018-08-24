package com.memtrip.eosreach.app.welcome.newaccount

import com.memtrip.mxandroid.MxViewIntent

sealed class NewAccountIntent : MxViewIntent {
    object Init : NewAccountIntent()
}