package com.memtrip.eosreach.api.blockproducer

data class RegisteredBlockProducer(
    val owner: String,
    val total_votes: String,
    val producer_key: String,
    val is_active: Int,
    val url: String
)