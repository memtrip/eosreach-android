package com.memtrip.eosreach.app.welcome.createaccount

import com.memtrip.eosreach.app.issue.createaccount.CreateAccountViewLayout
import com.memtrip.eosreach.app.issue.createaccount.CreateAccountViewRenderer
import com.memtrip.eosreach.app.issue.createaccount.CreateAccountViewState
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class CreateAccountRendererTest : Spek({

    val layout by memoized { mock<CreateAccountViewLayout>() }

    given("a CreateAccountRenderer") {

        val renderer by memoized { CreateAccountViewRenderer() }

        on("ViewState is not initialized") {
            renderer.layout(layout, CreateAccountViewState(CreateAccountViewState.View.OnProgress))

            it("shows the Progress") {
                verify(layout).showProgress()
            }
        }

        on("ViewState is initialized") {
            renderer.layout(layout, CreateAccountViewState(CreateAccountViewState.View.OnError))

            it("hides the Progress") {
                verify(layout).showError()
            }
        }
    }
})
