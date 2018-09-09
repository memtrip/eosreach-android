package com.memtrip.eosreach.app.transfer.confirm

data class TransferRequestData(
    val fromAccount: String,
    val toAccount: String,
    val quantity: String,
    val memo: String
)