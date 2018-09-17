package com.memtrip.eosreach.app.transaction

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
class TransactionLogListViewModelTest : Spek({

    given("a TransactionLogListViewModel") {

        val viewModel by memoized { TransactionLogListViewModel(mock()) }

        on("init") {

            val state = viewModel.states().test()

            viewModel.processIntents(Observable.just(TransactionLogListIntent.Init))

            it("emits initialized ViewState") {
                val states = state.values()
                assertThat(states.size).isEqualTo(2)
                assertThat(states[0]).isEqualTo(TransactionLogListViewState(TransactionLogListViewState.View.Idle))
                assertThat(states[1]).isEqualTo(TransactionLogListState(TransactionLogListViewState.View.OnProgress))
            }
        }
    }
})
