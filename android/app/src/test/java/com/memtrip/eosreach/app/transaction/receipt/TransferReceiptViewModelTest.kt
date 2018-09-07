package com.memtrip.eosreach.app.transaction.receipt

import com.nhaarman.mockito_kotlin.mock

import io.reactivex.Observable
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class TransferReceiptViewModelTest : Spek({

    given("a TransferReceiptViewModel") {

        val viewModel by memoized { TransactionReceiptViewModel(mock()) }

        on("init") {

            val state = viewModel.states().test()

            viewModel.processIntents(Observable.just(TransactionReceiptIntent.Init))

            it("emits initialized ViewState") {
                val states = state.values()
                assertThat(states.size).isEqualTo(2)
                assertThat(states[0]).isEqualTo(TransactionReceiptViewState(TransactionReceiptViewState.View.Idle))
                assertThat(states[1]).isEqualTo(TransferReceiptState(TransactionReceiptViewState.View.OnProgress))
            }
        }
    }
})
