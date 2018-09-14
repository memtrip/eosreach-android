package com.memtrip.eosreach.app.account.resources.manage

import com.memtrip.eosreach.app.account.resources.manage.manageram.ManageRamIntent
import com.memtrip.eosreach.app.account.resources.manage.manageram.ManageRamViewModel
import com.memtrip.eosreach.app.account.resources.manage.manageram.ManageRamViewState
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
class ManageRamViewModelTest : Spek({

    given("a ManageRamViewModel") {

        val viewModel by memoized { ManageRamViewModel(mock()) }

        on("init") {

            val state = viewModel.states().test()

            viewModel.processIntents(Observable.just(ManageRamIntent.Init))

            it("emits initialized ViewState") {
                val states = state.values()
                assertThat(states.size).isEqualTo(2)
                assertThat(states[0]).isEqualTo(ManageRamViewState(ManageRamViewState.View.Idle))
                assertThat(states[1]).isEqualTo(ManageRamState(ManageRamViewState.View.OnProgress))
            }
        }
    }
})
