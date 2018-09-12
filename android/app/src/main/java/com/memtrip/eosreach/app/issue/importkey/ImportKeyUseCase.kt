package com.memtrip.eosreach.app.issue.importkey

import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.api.accountforkey.AccountForKeyError
import com.memtrip.eosreach.api.accountforkey.AccountForPublicKeyRequest
import com.memtrip.eosreach.api.accountforkey.AccountsForPublicKey
import com.memtrip.eosreach.db.account.InsertAccountsForPublicKey
import com.memtrip.eosreach.utils.RxSchedulers

import com.memtrip.eosreach.wallet.EosKeyManager

import io.reactivex.Single

import javax.inject.Inject

class ImportKeyUseCase @Inject constructor(
    private val accountForKeyRequest: AccountForPublicKeyRequest,
    private val eosKeyManager: EosKeyManager,
    private val insertAccountsForPublicKey: InsertAccountsForPublicKey,
    private val rxSchedulers: RxSchedulers
) {

    fun importKey(privateKey: String): Single<Result<AccountsForPublicKey, AccountForKeyError>> = try {
        eosKeyManager.createEosPrivateKey(privateKey).flatMap { eosPrivateKey ->
            eosKeyManager.importPrivateKey(eosPrivateKey)
                .observeOn(rxSchedulers.main())
                .subscribeOn(rxSchedulers.background())
                .flatMap {
                    accountForKeyRequest.getAccountsForKey(it)
                }.flatMap { result ->
                    if (result.success) {
                        if (result.data!!.accounts.isEmpty()) {
                            Single.just(Result<AccountsForPublicKey, AccountForKeyError>(AccountForKeyError.NoAccounts))
                        } else {
                            insertAccountsForPublicKey.replace(
                                result.data.publicKey,
                                result.data.accounts
                            ).map {
                                result
                            }
                        }
                    } else {
                        Single.just(result)
                    }
                }.onErrorReturn {
                    Result(AccountForKeyError.Generic)
                }
        }
    } catch (e: Exception) {
        Single.just(Result<AccountsForPublicKey, AccountForKeyError>(AccountForKeyError.InvalidPrivateKey))
    }
}