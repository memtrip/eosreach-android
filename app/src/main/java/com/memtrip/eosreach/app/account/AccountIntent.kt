package com.memtrip.eosreach.app.account

import com.memtrip.mxandroid.MxViewIntent

sealed class AccountIntent : MxViewIntent {
    data class Init(val accountName: String) : AccountIntent()
}