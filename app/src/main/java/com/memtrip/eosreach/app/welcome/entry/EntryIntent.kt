package com.memtrip.eosreach.app.welcome.entry

import com.memtrip.mxandroid.MxViewIntent

sealed class EntryIntent : MxViewIntent {
    object Init : EntryIntent()
}