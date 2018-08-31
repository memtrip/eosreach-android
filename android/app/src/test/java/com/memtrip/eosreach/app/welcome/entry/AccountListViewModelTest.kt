package com.memtrip.eosreach.app.welcome.entry

import com.memtrip.eosreach.app.welcome.EntryIntent
import com.memtrip.eosreach.app.welcome.EntryViewModel
import com.memtrip.eosreach.app.welcome.EntryViewState
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

        val viewModel by memoized { EntryViewModel(mock()) }

        on("init") {

            val state = viewModel.states().test()

            viewModel.processIntents(Observable.just(EntryIntent.Init))

            it("emits initialized ViewState") {
                val states = state.values()
                assertThat(states.size).isEqualTo(2)
                assertThat(states[0]).isEqualTo(EntryViewState(EntryViewState.View.Idle))
                assertThat(states[1]).isEqualTo(AccountListState(EntryViewState.View.OnProgress))
            }
        }
    }
})
