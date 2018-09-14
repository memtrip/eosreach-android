package com.memtrip.eosreach.app.account.resources.manage.manageram.form

import com.memtrip.eosreach.app.account.resources.manage.manageram.RamFormViewLayout
import com.memtrip.eosreach.app.account.resources.manage.manageram.RamFormViewRenderer
import com.memtrip.eosreach.app.account.resources.manage.manageram.RamFormViewState
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class ManageRamFormRendererTest : Spek({

    val layout by memoized { mock<RamFormViewLayout>() }

    given("a ManageRamFormRenderer") {

        val renderer by memoized { RamFormViewRenderer() }

        on("ViewState is not initialized") {
            renderer.layout(layout, RamFormViewState(RamFormViewState.View.OnProgress))

            it("shows the Progress") {
                verify(layout).showProgress()
            }
        }

        on("ViewState is initialized") {
            renderer.layout(layout, RamFormViewState(RamFormViewState.View.OnError))

            it("hides the Progress") {
                verify(layout).showError()
            }
        }
    }
})
