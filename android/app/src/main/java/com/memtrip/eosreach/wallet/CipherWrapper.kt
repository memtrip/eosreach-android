package com.memtrip.eosreach.wallet

import android.util.Base64
import java.security.Key
import javax.crypto.Cipher

/**
 * https://github.com/temyco/security-workshop-sample/blob/master/app/src/stages/stage1/level1/java/co/temy/securitysample/encryption/CipherWrapper.kt
 */
class CipherWrapper {

    private val cipher: Cipher = Cipher.getInstance(TRANSFORMATION_ASYMMETRIC)

    fun encrypt(data: ByteArray, key: Key?): String {
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val bytes = cipher.doFinal(data)
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    fun decrypt(data: String, key: Key?): ByteArray {
        cipher.init(Cipher.DECRYPT_MODE, key)
        val encryptedData = Base64.decode(data, Base64.DEFAULT)
        return cipher.doFinal(encryptedData)
    }

    companion object {
        var TRANSFORMATION_ASYMMETRIC = "RSA/ECB/PKCS1Padding"
    }
}
