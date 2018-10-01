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

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.math.BigInteger
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.util.Calendar
import javax.security.auth.x500.X500Principal

/**
 * https://github.com/temyco/security-workshop-sample/blob/master/app/src/stages/stage1/level1/java/co/temy/securitysample/encryption/KeyStoreWrapper.kt
 */
class KeyStoreWrapper(private val context: Context) {

    private val keyStore: KeyStore = createAndroidKeyStore()

    fun getAndroidKeyStoreAsymmetricKeyPair(alias: String): KeyPair? {
        val privateKey = keyStore.getKey(alias, null) as PrivateKey?
        val publicKey = keyStore.getCertificate(alias)?.publicKey

        return if (privateKey != null && publicKey != null) {
            KeyPair(publicKey, privateKey)
        } else {
            null
        }
    }

    fun deleteEntry(alias: String) {
        keyStore.deleteEntry(alias)
    }

    fun createAndroidKeyStoreAsymmetricKey(alias: String): KeyPair {
        val generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initGeneratorWithKeyGenParameterSpec(generator, alias)
        } else {
            initGeneratorWithKeyPairGeneratorSpec(generator, alias)
        }

        return generator.generateKeyPair()
    }

    fun removeAndroidKeyStoreKey(alias: String) = keyStore.deleteEntry(alias)

    @Suppress("DEPRECATION")
    private fun initGeneratorWithKeyPairGeneratorSpec(generator: KeyPairGenerator, alias: String) {
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.YEAR, 20)

        val builder = KeyPairGeneratorSpec.Builder(context)
                .setAlias(alias)
                .setSerialNumber(BigInteger.ONE)
                .setSubject(X500Principal("CN=$alias CA Certificate"))
                .setStartDate(startDate.time)
                .setEndDate(endDate.time)

        generator.initialize(builder.build())
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun initGeneratorWithKeyGenParameterSpec(generator: KeyPairGenerator, alias: String) {
        val builder = KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
        generator.initialize(builder.build())
    }

    private fun createAndroidKeyStore(): KeyStore {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        return keyStore
    }
}