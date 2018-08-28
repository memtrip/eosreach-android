package com.memtrip.eosreach.wallet

import android.annotation.TargetApi
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.util.Base64
import com.memtrip.eosreach.R
import java.io.ByteArrayInputStream

import java.io.ByteArrayOutputStream

import java.security.KeyPairGenerator

import java.math.BigInteger
import java.security.KeyStore

import java.util.Calendar
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.security.auth.x500.X500Principal

@Suppress("DEPRECATION")
class WalletApi21(
    private val application: Application,
    private val sharedPreferences: SharedPreferences = application.getSharedPreferences(
        application.getString(R.string.app_shared_preferences_package),
        Context.MODE_PRIVATE),
    private val keyStore: KeyStore = with(KeyStore.getInstance("AndroidKeyStore")) {
        load(null)
        this
    }
) : Wallet {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun create(walletName: String) {

        if (!keyStore.containsAlias(walletName)) {
            val spec = KeyPairGeneratorSpec.Builder(application)
                .setAlias(walletName)
                .setSubject(X500Principal("CN=$walletName"))
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

    override fun importKey(walletName: String, privateKey: ByteArray) {
        val privateKeyEntry = keyStore.getEntry(walletName, null) as KeyStore.PrivateKeyEntry

        val inputCipher = Cipher.getInstance(RSA_MODE, PROVIDER)
        inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.certificate.publicKey)

        val outputStream = ByteArrayOutputStream()
        val cipherOutputStream = CipherOutputStream(outputStream, inputCipher)
        cipherOutputStream.write(privateKey)
        cipherOutputStream.close()

        sharedPreferences
            .edit()
            .putString("wallet-$walletName", Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT))
            .apply()
    }

    @Throws(Wallet.NotFoundException::class)
    override fun getKey(walletName: String): ByteArray {

        val privateKey = sharedPreferences.getString("wallet-$walletName", null)

        if (privateKey != null) {

            val encryptedPrivateKey = Base64.decode(privateKey, Base64.DEFAULT)

            val privateKeyEntry = keyStore.getEntry(walletName, null) as KeyStore.PrivateKeyEntry

            val output = with (Cipher.getInstance(RSA_MODE, PROVIDER)) {
                init(Cipher.DECRYPT_MODE, privateKeyEntry.privateKey)
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
            throw Wallet.NotFoundException()
        }
    }

    companion object {
        const val RSA_MODE = "RSA/ECB/PKCS1Padding"
        const val PROVIDER = "AndroidOpenSSL"
    }
}