package com.memtrip.eosreach.app.transfer.confirm

import com.memtrip.eosreach.api.balance.ContractAccountBalance

data class TransferRequestData(
    val fromAccount: String,
    val toAccount: String,
    val quantity: String,
    val memo: String,
    val contractAccountBalance: ContractAccountBalance
)