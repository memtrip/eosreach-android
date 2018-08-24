package com.memtrip.eosreach.app.welcome.accountcreated

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class AccountCreatedRendererTest : Spek({

    val layout by memoized { mock<AccountCreatedViewLayout>() }

    given("a AccountCreatedRenderer") {

        val renderer by memoized { AccountCreatedViewRenderer() }

        on("ViewState is not initialized") {
            renderer.layout(layout, AccountCreatedViewState(AccountCreatedViewState.View.OnProgress))

            it("shows the Progress") {
                verify(layout).showProgress()
            }
        }

        on("ViewState is initialized") {
            renderer.layout(layout, AccountCreatedViewState(AccountCreatedViewState.View.OnError))

            it("hides the Progress") {
                verify(layout).showError()
            }
        }
    }
})
