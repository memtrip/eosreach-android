package com.memtrip.eosreach.wallet

import android.annotation.TargetApi
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.util.Base64
import android.util.Log
import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eosreach.R
import io.reactivex.Observable
import io.reactivex.Single
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.util.Calendar
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.security.auth.x500.X500Principal

@Suppress("DEPRECATION")
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class EosKeyManagerApi21(
    private val application: Application,
    private val sharedPreferences: SharedPreferences = application.getSharedPreferences(
        application.getString(R.string.app_keys_shared_preferences_package),
        Context.MODE_PRIVATE),
    private val keyStore: KeyStore = with(KeyStore.getInstance("AndroidKeyStore")) {
        load(null)
        this
    }
) : EosKeyManager {

    override fun importPrivateKey(eosPrivateKey: EosPrivateKey): Single<String> {
        val keyAlias = eosPrivateKey.publicKey.toString()
        createRsaKeyPair(keyAlias)
        encryptAndSavePrivateKey(keyAlias, eosPrivateKey)
        return Single.just(keyAlias)
    }

    private fun createRsaKeyPair(keyAlias: String) {
        if (!keyStore.containsAlias(keyAlias)) {
            val spec = KeyPairGeneratorSpec.Builder(application)
                .setAlias(keyAlias)
                .setSubject(X500Principal("CN=$keyAlias"))
                .setSerialNumber(BigInteger.TEN)
                .setStartDate(Calendar.getInstance().time)
                .setEndDate(with (Calendar.getInstance()) {
                    add(Calendar.YEAR, 30)
                    this
                }.time).build()

            val keyPairGenerator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore")
            keyPairGenerator.initialize(spec)
            keyPairGenerator.generateKeyPair()
        }
    }

    private fun encryptAndSavePrivateKey(keyAlias: String, eosPrivateKey: EosPrivateKey) {

        val rsaEncryptionKeyEntry = keyStore.getEntry(keyAlias, null) as KeyStore.PrivateKeyEntry

        val inputCipher = Cipher.getInstance(RSA_MODE, PROVIDER)
        inputCipher.init(Cipher.ENCRYPT_MODE, rsaEncryptionKeyEntry.certificate.publicKey)

        val outputStream = ByteArrayOutputStream()
        val cipherOutputStream = CipherOutputStream(outputStream, inputCipher)
        cipherOutputStream.write(eosPrivateKey.bytes)
        cipherOutputStream.close()

        sharedPreferences
            .edit()
            .putString(keyAlias, Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT))
            .apply()
    }

    @Throws(EosKeyManager.NotFoundException::class)
    override fun getPrivateKey(eosPublicKey: String): ByteArray {

        val encodedEncryptedPrivateKey = sharedPreferences.getString(eosPublicKey, null)

        if (encodedEncryptedPrivateKey != null) {

            val encryptedPrivateKey = Base64.decode(encodedEncryptedPrivateKey, Base64.DEFAULT)

            val rsaEncryptionKeyEntry = keyStore.getEntry(eosPublicKey, null) as KeyStore.PrivateKeyEntry

            val output = with (Cipher.getInstance(RSA_MODE, PROVIDER)) {
                init(Cipher.DECRYPT_MODE, rsaEncryptionKeyEntry.privateKey)
                this
            }

            val values = ArrayList<Byte>()
            CipherInputStream(ByteArrayInputStream(encryptedPrivateKey), output).bufferedReader().use {
                values.add(it.read().toByte())
            }

            val bytes = ByteArray(values.size)
            for (i in bytes.indices) {
                bytes[i] = values[i]
            }

            return bytes
        } else {
            throw EosKeyManager.NotFoundException()
        }
    }

    override fun publicKeyExists(eosPublicKey: String): Boolean {
        return sharedPreferences.getString(eosPublicKey, null) != null
    }

    override fun getAllPublicKeys(): Observable<String> {
        return Observable.fromIterable(sharedPreferences.all.entries.map { it.key })
    }

    override fun getPrivateKeys(): Single<List<EosPrivateKey>> {
        return Single.create<List<EosPrivateKey>> { single ->
            single.onSuccess(sharedPreferences.all.entries.map { EosPrivateKey(getPrivateKey(it.key)) })
        }
    }

    companion object {
        const val RSA_MODE = "RSA/ECB/PKCS1Padding"
        const val PROVIDER = "AndroidOpenSSL"
    }
}