package com.memtrip.eosreach.app.transfer.confirm

import com.memtrip.eosreach.api.balance.Balance

data class TransferRequestData(
    val fromAccount: String,
    val toAccount: String,
    val quantity: Balance,
    val memo: String
)