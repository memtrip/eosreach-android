package com.memtrip.eosreach.api.blockproducer

data class RegisteredBlockProducer(
    val owner: String,
    val votesInEos: String,
    val producerKey: String,
    val isActive: Int,
    val url: String
)