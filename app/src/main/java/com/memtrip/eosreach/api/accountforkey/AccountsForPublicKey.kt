package com.memtrip.eosreach.api.accountforkey

data class AccountsForPublicKey(
    val publicKey: String,
    val accounts: List<String>
)