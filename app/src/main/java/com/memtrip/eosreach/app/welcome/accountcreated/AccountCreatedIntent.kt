package com.memtrip.eosreach.app.welcome.accountcreated

import com.memtrip.mxandroid.MxViewIntent

sealed class AccountCreatedIntent : MxViewIntent {
    object Init : AccountCreatedIntent()
}