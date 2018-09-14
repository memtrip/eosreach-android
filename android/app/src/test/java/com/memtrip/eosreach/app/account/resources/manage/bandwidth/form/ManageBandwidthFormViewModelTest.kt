package com.memtrip.eosreach.app.account.resources.manage.bandwidth.form

import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthFormIntent
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthFormViewModel
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthFormViewState
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
class ManageBandwidthFormViewModelTest : Spek({

    given("a ManageBandwidthFormViewModel") {

        val viewModel by memoized { BandwidthFormViewModel(mock()) }

        on("init") {

            val state = viewModel.states().test()

            viewModel.processIntents(Observable.just(BandwidthFormIntent.Init))

            it("emits initialized ViewState") {
                val states = state.values()
                assertThat(states.size).isEqualTo(2)
                assertThat(states[0]).isEqualTo(BandwidthFormViewState(BandwidthFormViewState.View.Idle))
                assertThat(states[1]).isEqualTo(ManageBandwidthFormState(BandwidthFormViewState.View.OnProgress))
            }
        }
    }
})
