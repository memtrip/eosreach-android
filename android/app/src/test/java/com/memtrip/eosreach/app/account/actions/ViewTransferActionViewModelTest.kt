package com.memtrip.eosreach.app.account.actions

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
class ViewTransferActionViewModelTest : Spek({

    given("a ViewTransferActionViewModel") {

        val viewModel by memoized { ViewTransferActionViewModel(mock()) }

        on("init") {

            val state = viewModel.states().test()

            viewModel.processIntents(Observable.just(ViewTransferActionIntent.Init))

            it("emits initialized ViewState") {
                val states = state.values()
                assertThat(states.size).isEqualTo(2)
                assertThat(states[0]).isEqualTo(ViewTransferActionViewState(ViewTransferActionViewState.View.Idle))
                assertThat(states[1]).isEqualTo(ViewTransferActionState(ViewTransferActionViewState.View.OnProgress))
            }
        }
    }
})
