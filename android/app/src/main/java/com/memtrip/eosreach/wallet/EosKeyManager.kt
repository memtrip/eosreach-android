package com.memtrip.eosreach.wallet

import com.memtrip.eos.core.crypto.EosPrivateKey
import io.reactivex.Observable
import io.reactivex.Single

interface EosKeyManager {
    fun importPrivateKey(eosPrivateKey: EosPrivateKey): Single<String>
    fun getPrivateKey(eosPublicKey: String): ByteArray
    fun publicKeyExists(eosPublicKey: String): Boolean
    fun getAllPublicKeys(): Observable<String>
    fun getPrivateKeys(): Single<List<EosPrivateKey>>

    class NotFoundException : RuntimeException()
}
