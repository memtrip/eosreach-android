package com.memtrip.eosreach.app.transfer

import com.memtrip.eosreach.app.transfer.form.TransferFormViewLayout
import com.memtrip.eosreach.app.transfer.form.TransferFormViewRenderer
import com.memtrip.eosreach.app.transfer.form.TransferFormViewState
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class TransferRendererTest : Spek({

    val layout by memoized { mock<TransferFormViewLayout>() }

    given("a TransferRenderer") {

        val renderer by memoized { TransferFormViewRenderer() }

        on("ViewState is not initialized") {
            renderer.layout(layout, TransferFormViewState(TransferFormViewState.View.OnProgress))

            it("shows the Progress") {
                verify(layout).showProgress()
            }
        }

        on("ViewState is initialized") {
            renderer.layout(layout, TransferFormViewState(TransferFormViewState.View.OnError))

            it("hides the Progress") {
                verify(layout).showError()
            }
        }
    }
})
