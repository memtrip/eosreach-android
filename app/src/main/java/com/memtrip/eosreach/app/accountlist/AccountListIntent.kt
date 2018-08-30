package com.memtrip.eosreach.app.accountlist

import com.memtrip.mxandroid.MxViewIntent

sealed class AccountListIntent : MxViewIntent {
    object Init : AccountListIntent()
}