package com.memtrip.eosreach.app.accountlist

import com.memtrip.eosreach.api.accountforkey.AccountForPublicKeyRequest
import com.memtrip.eosreach.db.account.AccountEntity

import com.memtrip.eosreach.db.account.InsertAccountsForPublicKey
import com.memtrip.eosreach.wallet.EosKeyManager
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

import javax.inject.Inject

class AccountListUseCase @Inject internal constructor(
    private val accountForPublicKeyRequest: AccountForPublicKeyRequest,
    private val insertAccountsForPublicKey: InsertAccountsForPublicKey,
    private val eosKeyManager: EosKeyManager
) {

    fun refreshAccounts(): Completable {
        return eosKeyManager.getAllPublicKeys().map { publicKey ->
            accountForPublicKeyRequest.getAccountsForKey(publicKey).flatMap { result ->
                if (result.success) {
                    Single.just(result.data!!)
                } else {
                    Single.error(RefreshAccountsFailed())
                }
            }.onErrorResumeNext {
                Single.error(RefreshAccountsFailed())
            }.flatMapCompletable { accountsForPublicKey ->
                insertAccountsForPublicKey.replace(
                    accountsForPublicKey.publicKey,
                    accountsForPublicKey.accounts
                ).toCompletable()
            }
        }.flatMapCompletable { it }
    }

    class RefreshAccountsFailed : IllegalStateException()
}