package com.memtrip.eosreach.app.welcome

import com.memtrip.mxandroid.MxViewIntent

sealed class EntryIntent : MxViewIntent {
    object Init : EntryIntent()
}