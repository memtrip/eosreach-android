package com.memtrip.eosreach.app.account.actions

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class ActionsRendererTest : Spek({

    val layout by memoized { mock<ActionsViewLayout>() }

    given("a ActionsRenderer") {

        val renderer by memoized { ActionsViewRenderer() }

        on("ViewState is not initialized") {
            renderer.layout(layout, ActionsViewState(ActionsViewState.View.OnProgress))

            it("shows the Progress") {
                verify(layout).showProgress()
            }
        }

        on("ViewState is initialized") {
            renderer.layout(layout, ActionsViewState(ActionsViewState.View.OnError))

            it("hides the Progress") {
                verify(layout).showError()
            }
        }
    }
})
