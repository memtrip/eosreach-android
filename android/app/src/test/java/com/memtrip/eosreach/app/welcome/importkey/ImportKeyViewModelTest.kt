package com.memtrip.eosreach.app.welcome.importkey

import com.memtrip.eosreach.app.issue.importkey.ImportKeyIntent
import com.memtrip.eosreach.app.issue.importkey.ImportKeyViewModel
import com.memtrip.eosreach.app.issue.importkey.ImportKeyViewState
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
class ImportKeyViewModelTest : Spek({

    given("a ImportKeyViewModel") {

        val viewModel by memoized { ImportKeyViewModel(mock()) }

        on("init") {

            val state = viewModel.states().test()

            viewModel.processIntents(Observable.just(ImportKeyIntent.Init))

            it("emits initialized ViewState") {
                val states = state.values()
                assertThat(states.size).isEqualTo(2)
                assertThat(states[0]).isEqualTo(ImportKeyViewState(ImportKeyViewState.View.Idle))
                assertThat(states[1]).isEqualTo(ImportKeyViewState(ImportKeyViewState.View.OnProgress))
            }
        }
    }
})
