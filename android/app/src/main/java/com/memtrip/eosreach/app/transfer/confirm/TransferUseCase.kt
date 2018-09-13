package com.memtrip.eosreach.app.transfer.confirm

import com.memtrip.eos.http.rpc.model.transaction.response.TransactionCommitted
import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.api.transfer.TransferError
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
        quantity: String,
        memo: String
    ): Single<Result<TransactionCommitted, TransferError>> {

        return getAccountByName.select(fromAccount).flatMap { accountEntity ->
            eosKeyManager.getPrivateKey(accountEntity.publicKey).flatMap { privateKey ->
                transferRequest.transfer(
                    accountEntity.accountName,
                    toAccount,
                    quantity,
                    memo,
                    privateKey)
            }.onErrorReturn {
                Result(TransferError(it.message!!))
            }
        }
    }
}