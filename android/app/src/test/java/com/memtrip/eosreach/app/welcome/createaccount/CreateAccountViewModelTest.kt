package com.memtrip.eosreach.app.welcome.createaccount

import com.memtrip.eosreach.app.issue.createaccount.CreateAccountIntent
import com.memtrip.eosreach.app.issue.createaccount.CreateAccountViewModel
import com.memtrip.eosreach.app.issue.createaccount.CreateAccountViewState
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
class CreateAccountViewModelTest : Spek({

    given("a CreateAccountViewModel") {

        val viewModel by memoized { CreateAccountViewModel(mock()) }

        on("init") {

            val state = viewModel.states().test()

            viewModel.processIntents(Observable.just(CreateAccountIntent.Init))

            it("emits initialized ViewState") {
                val states = state.values()
                assertThat(states.size).isEqualTo(2)
                assertThat(states[0]).isEqualTo(CreateAccountViewState(CreateAccountViewState.View.Idle))
                assertThat(states[1]).isEqualTo(CreateAccountViewState(CreateAccountViewState.View.OnCreateAccountProgress))
            }
        }
    }
})
