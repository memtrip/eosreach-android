package com.memtrip.eosreach.app.welcome.importkey

import com.memtrip.eos.core.crypto.EosPrivateKey

import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.api.accountforkey.AccountForKeyError
import com.memtrip.eosreach.api.accountforkey.AccountForPublicKeyRequest
import com.memtrip.eosreach.api.accountforkey.AccountsForPublicKey
import com.memtrip.eosreach.storage.InsertAccountsForPublicKey
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
        }.map { result ->
            if (result.success) {
                insertAccountsForPublicKey.insert(
                    result.response!!.publicKey,
                    result.response.accounts)
            }
            result
        }.onErrorReturn {
            it.printStackTrace()
            Result(AccountForKeyError.Generic)
        }
    } catch (e: InvalidPrivateKey) {
        Single.just(Result<AccountsForPublicKey, AccountForKeyError>(AccountForKeyError.InvalidPrivateKey))
    } catch (e: PrivateKeyAlreadyImported) {
        Single.just(Result<AccountsForPublicKey, AccountForKeyError>(AccountForKeyError.PrivateKeyAlreadyImported))
    }

    @Throws(InvalidPrivateKey::class, PrivateKeyAlreadyImported::class)
    private fun createEosPrivateKey(privateKey: String): EosPrivateKey {
        try {
            val eosPrivateKey = EosPrivateKey(privateKey)
            eosKeyManager.throwIfPublicKeyExists(eosPrivateKey.publicKey.toString())
            return eosPrivateKey
        } catch (e: Exception) {
            if (e is PrivateKeyAlreadyImported) {
                throw PrivateKeyAlreadyImported()
            } else {
                throw InvalidPrivateKey()
            }
        }
    }

    private class InvalidPrivateKey : Exception()

    private class PrivateKeyAlreadyImported : Exception()
}