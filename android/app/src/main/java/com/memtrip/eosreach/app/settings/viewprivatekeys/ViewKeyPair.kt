package com.memtrip.eosreach.app.settings.viewprivatekeys

import com.memtrip.eos.core.crypto.EosPrivateKey

data class ViewKeyPair(
    val eosPrivateKey: EosPrivateKey,
    val associatedAccounts: List<String>
)