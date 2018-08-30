package com.memtrip.eosreach.app.account

import com.memtrip.mxandroid.MxViewIntent

sealed class AccountIntent : MxViewIntent {
    object Init : AccountIntent()
    object Retry : AccountIntent()
}