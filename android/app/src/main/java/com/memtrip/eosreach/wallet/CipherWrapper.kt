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
