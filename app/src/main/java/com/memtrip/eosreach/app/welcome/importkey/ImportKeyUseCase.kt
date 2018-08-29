package com.memtrip.eosreach.app.welcome.importkey

import com.memtrip.eos.core.crypto.EosPrivateKey

import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.api.accountforkey.AccountForKeyError
import com.memtrip.eosreach.api.accountforkey.AccountForPublicKeyRequest
import com.memtrip.eosreach.api.accountforkey.AccountsForPublicKey
import com.memtrip.eosreach.db.InsertAccountsForPublicKey
import com.memtrip.eosreach.wallet.EosKeyManager

import io.reactivex.Single

import javax.inject.Inject

class ImportKeyUseCase @Inject constructor(
    private val accountForKeyRequest: AccountForPublicKeyRequest,
    private val eosKeyManager: EosKeyManager,
    private val insertAccountsForPublicKey: InsertAccountsForPublicKey
) {

    fun importKey(privateKey: String): Single<Result<AccountsForPublicKey, AccountForKeyError>> = try {
        eosKeyManager.importPrivateKey(createEosPrivateKey(privateKey)).flatMap {
            accountForKeyRequest.getAccountsForKey(it)
        }.flatMap { result ->
            if (result.success) {
                insertAccountsForPublicKey.insert(
                    result.response!!.publicKey,
                    result.response.accounts
                ).map {
                    result
                }
            } else {
                Single.just(result)
            }
        }.onErrorReturn {
            it.printStackTrace()
            Result(AccountForKeyError.Generic)
        }
    } catch (e: PrivateKeyAlreadyImported) {
        Single.just(Result<AccountsForPublicKey, AccountForKeyError>(AccountForKeyError.PrivateKeyAlreadyImported))
    } catch (e: Exception) {
        Single.just(Result<AccountsForPublicKey, AccountForKeyError>(AccountForKeyError.InvalidPrivateKey))
    }

    @Throws(Exception::class, PrivateKeyAlreadyImported::class)
    private fun createEosPrivateKey(privateKey: String): EosPrivateKey {

        val eosPrivateKey = EosPrivateKey(privateKey)

        if (eosKeyManager.publicKeyExists(eosPrivateKey.publicKey.toString())) {
            throw PrivateKeyAlreadyImported()
        } else {
            return eosPrivateKey
        }
    }

    private class PrivateKeyAlreadyImported : Exception()
}