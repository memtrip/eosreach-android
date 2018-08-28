package com.memtrip.eosreach.app.welcome.accountlist

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
class AccountListViewModelTest : Spek({

    given("a AccountListViewModel") {

        val viewModel by memoized { AccountListViewModel(mock()) }

        on("init") {

            val state = viewModel.states().test()

            viewModel.processIntents(Observable.just(AccountListIntent.Init))

            it("emits initialized ViewState") {
                val states = state.values()
                assertThat(states.size).isEqualTo(2)
                assertThat(states[0]).isEqualTo(AccountListViewState(AccountListViewState.View.Idle))
                assertThat(states[1]).isEqualTo(AccountListState(AccountListViewState.View.OnProgress))
            }
        }
    }
})
