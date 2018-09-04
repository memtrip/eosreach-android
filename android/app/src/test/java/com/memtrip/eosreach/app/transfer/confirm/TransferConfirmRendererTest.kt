package com.memtrip.eosreach.app.transfer.confirm

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class TransferConfirmRendererTest : Spek({

    val layout by memoized { mock<TransferConfirmViewLayout>() }

    given("a TransferConfirmRenderer") {

        val renderer by memoized { TransferConfirmViewRenderer() }

        on("ViewState is not initialized") {
            renderer.layout(layout, TransferConfirmViewState(TransferConfirmViewState.View.OnProgress))

            it("shows the Progress") {
                verify(layout).showProgress()
            }
        }

        on("ViewState is initialized") {
            renderer.layout(layout, TransferConfirmViewState(TransferConfirmViewState.View.OnError))

            it("hides the Progress") {
                verify(layout).showError()
            }
        }
    }
})
