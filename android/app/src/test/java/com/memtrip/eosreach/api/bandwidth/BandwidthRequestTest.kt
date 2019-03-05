package com.memtrip.eosreach.api.bandwidth

import com.memtrip.eos.chain.actions.transaction.TransactionContext
import com.memtrip.eos.chain.actions.transaction.account.DelegateBandwidthChain
import com.memtrip.eos.chain.actions.transaction.account.UnDelegateBandwidthChain
import com.memtrip.eos.core.crypto.EosPrivateKey
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
import java.util.Date

@RunWith(JUnitPlatform::class)
class BandwidthRequestTest : Spek({

    given("a BandwidthRequest") {

        val delegateBandwidthChain by memoized { mock<DelegateBandwidthChain>() }
        val unDelegateBandwidthChain by memoized { mock<UnDelegateBandwidthChain>() }

        val bandwidthRequest by memoized {
            BandwidthRequestImpl(
                delegateBandwidthChain,
                unDelegateBandwidthChain,
                TestRxSchedulers())
        }

        on("delegate bandwidth error") {

            val fromAccount = "memtripissue"
            val netAmount = "0.1000 SYS"
            val cpuAmount = "0.1000 SYS"
            val authorizingPrivateKey = EosPrivateKey("5KWEWsH7B8DpiGzWuTqCFvuYc7DydHjeTzX6ZQYYK3jmfJb7rbP")
            val date = Date()

            whenever(delegateBandwidthChain.delegateBandwidth(
                DelegateBandwidthChain.Args(
                    fromAccount,
                    fromAccount,
                    netAmount,
                    cpuAmount,
                    false
                ),
                TransactionContext(
                    fromAccount,
                    authorizingPrivateKey,
                    date
                )
            )).thenReturn(Single.error(SocketTimeoutException()))

            val result = bandwidthRequest.delegate(
                fromAccount,
                fromAccount,
                netAmount,
                cpuAmount,
                false,
                authorizingPrivateKey,
                date).blockingGet()

            it("returns a GenericError") {
                assertThat(result.apiError!!).isEqualTo(BandwidthError.GenericError)
            }
        }

        on("undelegate bandwidth error") {

            val fromAccount = "memtripissue"
            val netAmount = "0.1000 SYS"
            val cpuAmount = "0.1000 SYS"
            val authorizingPrivateKey = EosPrivateKey("5KWEWsH7B8DpiGzWuTqCFvuYc7DydHjeTzX6ZQYYK3jmfJb7rbP")
            val date = Date()

            whenever(unDelegateBandwidthChain.unDelegateBandwidth(
                UnDelegateBandwidthChain.Args(
                    fromAccount,
                    fromAccount,
                    netAmount,
                    cpuAmount
                ),
                TransactionContext(
                    fromAccount,
                    authorizingPrivateKey,
                    date
                )
            )).thenReturn(Single.error(SocketTimeoutException()))

            val result = bandwidthRequest.unDelegate(
                fromAccount,
                fromAccount,
                netAmount,
                cpuAmount,
                authorizingPrivateKey,
                date).blockingGet()

            it("returns a GenericError") {
                assertThat(result.apiError!!).isEqualTo(BandwidthError.GenericError)
            }
        }
    }
})
