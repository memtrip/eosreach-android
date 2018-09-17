package com.memtrip.eosreach.app.transaction

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class TransactionLogListRendererTest : Spek({

    val layout by memoized { mock<TransactionLogListViewLayout>() }

    given("a TransactionLogListRenderer") {

        val renderer by memoized { TransactionLogListViewRenderer() }

        on("ViewState is not initialized") {
            renderer.layout(layout, TransactionLogListViewState(TransactionLogListViewState.View.OnProgress))

            it("shows the Progress") {
                verify(layout).showProgress()
            }
        }

        on("ViewState is initialized") {
            renderer.layout(layout, TransactionLogListViewState(TransactionLogListViewState.View.OnError))

            it("hides the Progress") {
                verify(layout).showError()
            }
        }
    }
})
