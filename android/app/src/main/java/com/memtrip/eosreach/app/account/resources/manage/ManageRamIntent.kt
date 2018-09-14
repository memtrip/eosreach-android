package com.memtrip.eosreach.app.account.resources.manage

import com.memtrip.mxandroid.MxViewIntent

sealed class ManageRamIntent : MxViewIntent {
    object Init : ManageRamIntent()
}