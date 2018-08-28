package com.memtrip.eosreach.app.welcome.accountlist

import com.memtrip.mxandroid.MxViewIntent

sealed class AccountListIntent : MxViewIntent {
    object Init : AccountListIntent()
    object Idle : AccountListIntent()
}