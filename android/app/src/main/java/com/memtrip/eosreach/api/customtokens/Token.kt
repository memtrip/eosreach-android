package com.memtrip.eosreach.api.customtokens

data class Token(
    val uuid: Double,
    val owner: String,
    val customtoken: String,
    val customasset: String
)