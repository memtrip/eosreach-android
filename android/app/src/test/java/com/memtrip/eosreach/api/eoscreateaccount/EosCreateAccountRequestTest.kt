package com.memtrip.eosreach.api.eoscreateaccount

import com.memtrip.eosreach.TestRxSchedulers
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.net.SocketTimeoutException

@RunWith(JUnitPlatform::class)
class EosCreateAccountRequestTest : Spek({

    given("a EosCreateAccountRequest") {

        val eosCreateAccountApi by memoized { mock<EosCreateAccountApi>() }

        val eosCreateAccountRequest by memoized {
            EosCreateAccountRequestImpl(
                eosCreateAccountApi,
                mock(),
                TestRxSchedulers())
        }

        on("create account error") {

            val purchaseToken = "E45"
            val accountName = "memtripissue"
            val publicKey = "EOS72FpVthazrVXhkQ8CEyWvMdo8VB2eVYNT7rhnJLESMUTD3FhNc"

            whenever(eosCreateAccountApi.createAccount(CreateAccountRequest(
                purchaseToken,
                accountName,
                publicKey
            ))).thenReturn(Single.error(SocketTimeoutException()))

            val result = eosCreateAccountRequest.createAccount(
                purchaseToken,
                accountName,
                publicKey).blockingGet()

            it("returns a GenericError") {
                assertThat(result.apiError!!).isEqualTo(EosCreateAccountError.GenericError)
            }
        }
    }
})
