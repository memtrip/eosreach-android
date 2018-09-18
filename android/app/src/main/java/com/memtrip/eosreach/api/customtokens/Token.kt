package com.memtrip.eosreach.api.customtokens

data class Token(
    val uuid: Long,
    val owner: String,
    val customtoken: String,
    val customasset: String
)