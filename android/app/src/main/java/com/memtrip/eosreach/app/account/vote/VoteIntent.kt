package com.memtrip.eosreach.app.account.vote

import com.memtrip.mxandroid.MxViewIntent

sealed class VoteIntent : MxViewIntent {
    object Init : VoteIntent()
}