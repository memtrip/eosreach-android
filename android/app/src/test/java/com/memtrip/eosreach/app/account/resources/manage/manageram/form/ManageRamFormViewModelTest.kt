package com.memtrip.eosreach.app.account.resources.manage.manageram.form

import com.memtrip.eosreach.app.account.resources.manage.manageram.RamFormIntent
import com.memtrip.eosreach.app.account.resources.manage.manageram.RamFormViewModel
import com.memtrip.eosreach.app.account.resources.manage.manageram.RamFormViewState
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
class ManageRamFormViewModelTest : Spek({

    given("a ManageRamFormViewModel") {

        val viewModel by memoized { RamFormViewModel(mock()) }

        on("init") {

            val state = viewModel.states().test()

            viewModel.processIntents(Observable.just(RamFormIntent.Init))

            it("emits initialized ViewState") {
                val states = state.values()
                assertThat(states.size).isEqualTo(2)
                assertThat(states[0]).isEqualTo(RamFormViewState(RamFormViewState.View.Idle))
                assertThat(states[1]).isEqualTo(ManageRamFormState(RamFormViewState.View.OnProgress))
            }
        }
    }
})
