/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.memtrip.eosreach.wallet

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eosreach.R
import com.memtrip.eosreach.db.sharedpreferences.RsaEncryptionVerified
import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class EosKeyManagerImpl @Inject constructor(
    private val keyStoreWrapper: KeyStoreWrapper,
    private val cipherWrapper: CipherWrapper,
    private val rsaEncryptionVerified: RsaEncryptionVerified,
    private val rxSchedulers: RxSchedulers,
    application: Application
) : EosKeyManager {

    private val sharedPreferences: SharedPreferences = application.getSharedPreferences(
        application.getString(R.string.app_keys_shared_preferences_package),
        Context.MODE_PRIVATE)

    override fun verifyDeviceSupportsRsaEncryption(): Single<Boolean> {
        return Single.create<Boolean> { single ->
            try {
                if (rsaEncryptionVerified.get()) {
                    single.onSuccess(true)
                } else {
                    val verifyKey = "VERIFY_RSA_SUPPORTED_KEY"
                    keyStoreWrapper.createAndroidKeyStoreAsymmetricKey(verifyKey)
                    val encryptPrivateKey = EosPrivateKey()
                    encryptAndSavePrivateKey(verifyKey, encryptPrivateKey)
                    val privateKeyBytes = getPrivateKeyBytes(verifyKey)
                    val decryptPrivateKey = EosPrivateKey(privateKeyBytes)

                    if (encryptPrivateKey.toString() == decryptPrivateKey.toString()) {
                        rsaEncryptionVerified.put(true)
                        sharedPreferences.edit().remove(verifyKey).apply()
                        keyStoreWrapper.deleteEntry(verifyKey)
                        single.onSuccess(true)
                    } else {
                        single.onSuccess(false)
                    }
                }
            } catch (e: Throwable) {
                single.onError(e)
            }
        }.observeOn(rxSchedulers.main()).subscribeOn(rxSchedulers.background())
    }

    override fun importPrivateKey(eosPrivateKey: EosPrivateKey): Single<String> {
        return Single.create<String> { single ->
            try {
                val keyAlias = eosPrivateKey.publicKey.toString()
                keyStoreWrapper.createAndroidKeyStoreAsymmetricKey(keyAlias)
                encryptAndSavePrivateKey(keyAlias, eosPrivateKey)
                single.onSuccess(keyAlias)
            } catch (e: Throwable) {
                single.onError(e)
            }
        }.observeOn(rxSchedulers.main()).subscribeOn(rxSchedulers.background())
    }

    private fun encryptAndSavePrivateKey(keyAlias: String, eosPrivateKey: EosPrivateKey) {
        val keyPair = keyStoreWrapper.getAndroidKeyStoreAsymmetricKeyPair(keyAlias)
        val encryptedData = cipherWrapper.encrypt(eosPrivateKey.bytes, keyPair?.public)
        sharedPreferences
            .edit()
            .putString(keyAlias, encryptedData)
            .apply()
    }

    override fun getPrivateKey(eosPublicKey: String): Single<EosPrivateKey> {
        return Single.create<EosPrivateKey> { single ->
            try {
                val privateKey = EosPrivateKey(getPrivateKeyBytes(eosPublicKey))
                single.onSuccess(privateKey)
            } catch (e: Throwable) {
                single.onError(e)
            }
        }.observeOn(rxSchedulers.main()).subscribeOn(rxSchedulers.background())
    }

    override fun publicKeyExists(eosPublicKey: String): Boolean {
        return sharedPreferences.getString(eosPublicKey, null) != null
    }

    override fun getAllPublicKeys(): List<String> {
        return sharedPreferences.all.entries.map { it.key }
    }

    override fun getPrivateKeys(): Single<List<EosPrivateKey>> {
        return Single.create<List<EosPrivateKey>> { single ->
            val entries = sharedPreferences.all.entries
            if (entries.isNotEmpty()) {
                single.onSuccess(entries.map { EosPrivateKey(getPrivateKeyBytes(it.key)) })
            } else {
                single.onError(EosKeyManager.NotFoundException())
            }
        }.observeOn(rxSchedulers.main()).subscribeOn(rxSchedulers.background())
    }

    override fun createEosPrivateKey(value: String): Single<EosPrivateKey> {
        return Single
            .create<EosPrivateKey> { single ->
                try {
                    val privateKey = EosPrivateKey(value)
                    single.onSuccess(privateKey)
                } catch (e: Throwable) {
                    single.onError(e)
                }
            }
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
    }

    override fun createEosPrivateKey(): Single<EosPrivateKey> {
        return Single
            .create<EosPrivateKey> { single ->
                try {
                    val privateKey = EosPrivateKey()
                    single.onSuccess(privateKey)
                } catch (e: Throwable) {
                    single.onError(e)
                }
            }
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
    }

    private fun getPrivateKeyBytes(keyAlias: String): ByteArray {

        val encodedEncryptedPrivateKey = sharedPreferences.getString(keyAlias, null)

        if (encodedEncryptedPrivateKey != null) {
            val keyPair = keyStoreWrapper.getAndroidKeyStoreAsymmetricKeyPair(keyAlias)
            return cipherWrapper.decrypt(encodedEncryptedPrivateKey, keyPair?.private)
        } else {
            throw EosKeyManager.NotFoundException()
        }
    }

    override fun removeKeystoreEntries(): Completable {
        return Completable.create { completable ->
            sharedPreferences.all.entries.map { alias ->
                keyStoreWrapper.deleteEntry(alias.key)
            }
            completable.onComplete()
        }.observeOn(rxSchedulers.main()).subscribeOn(rxSchedulers.background())
    }
}