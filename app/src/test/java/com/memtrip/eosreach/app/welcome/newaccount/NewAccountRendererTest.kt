package com.memtrip.eosreach.app.welcome.newaccount

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class NewAccountRendererTest : Spek({

    val layout by memoized { mock<NewAccountViewLayout>() }

    given("a NewAccountRenderer") {

        val renderer by memoized { NewAccountViewRenderer() }

        on("ViewState is not initialized") {
            renderer.layout(layout, NewAccountViewState(NewAccountViewState.View.OnProgress))

            it("shows the Progress") {
                verify(layout).showProgress()
            }
        }

        on("ViewState is initialized") {
            renderer.layout(layout, NewAccountViewState(NewAccountViewState.View.OnError))

            it("hides the Progress") {
                verify(layout).showError()
            }
        }
    }
})
