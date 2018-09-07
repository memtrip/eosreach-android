package com.memtrip.eosreach.app.transaction

import com.memtrip.eosreach.app.transaction.log.TransactionLogIntent
import com.memtrip.eosreach.app.transaction.log.TransactionLogViewModel
import com.memtrip.eosreach.app.transaction.log.TransactionLogViewState
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
class TransactionLogViewModelTest : Spek({

    given("a TransactionLogViewModel") {

        val viewModel by memoized { TransactionLogViewModel(mock()) }

        on("init") {

            val state = viewModel.states().test()

            viewModel.processIntents(Observable.just(TransactionLogIntent.Init))

            it("emits initialized ViewState") {
                val states = state.values()
                assertThat(states.size).isEqualTo(2)
                assertThat(states[0]).isEqualTo(TransactionLogViewState(TransactionLogViewState.View.Idle))
                assertThat(states[1]).isEqualTo(TransactionLogState(TransactionLogViewState.View.OnProgress))
            }
        }
    }
})
