package com.memtrip.eosreach.app.transfer.confirm

import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.eosreach.api.transfer.TransferError
import com.memtrip.eosreach.api.transfer.TransferReceipt
import com.memtrip.eosreach.api.transfer.TransferRequest

import com.memtrip.eosreach.db.account.GetAccountByName
import com.memtrip.eosreach.wallet.EosKeyManager
import io.reactivex.Single

import javax.inject.Inject

class TransferUseCase @Inject internal constructor(
    private val transferRequest: TransferRequest,
    private val getAccountByName: GetAccountByName,
    private val eosKeyManager: EosKeyManager
) {

    fun transfer(
        fromAccount: String,
        toAccount: String,
        quantity: Balance,
        memo: String
    ): Single<Result<TransferReceipt, TransferError>> {
        return getAccountByName.select(fromAccount).flatMap { account ->
            transferRequest.transfer(
                account.accountName,
                toAccount,
                quantity,
                memo,
                EosPrivateKey(eosKeyManager.getPrivateKey(account.publicKey))
            )
        }
    }
}