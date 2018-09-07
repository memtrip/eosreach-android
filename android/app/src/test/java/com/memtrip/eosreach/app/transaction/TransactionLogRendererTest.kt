package com.memtrip.eosreach.app.transaction

import com.memtrip.eosreach.app.transaction.log.TransactionLogViewLayout
import com.memtrip.eosreach.app.transaction.log.TransactionLogViewRenderer
import com.memtrip.eosreach.app.transaction.log.TransactionLogViewState
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class TransactionLogRendererTest : Spek({

    val layout by memoized { mock<TransactionLogViewLayout>() }

    given("a TransactionLogRenderer") {

        val renderer by memoized { TransactionLogViewRenderer() }

        on("ViewState is not initialized") {
            renderer.layout(layout, TransactionLogViewState(TransactionLogViewState.View.OnProgress))

            it("shows the Progress") {
                verify(layout).showProgress()
            }
        }

        on("ViewState is initialized") {
            renderer.layout(layout, TransactionLogViewState(TransactionLogViewState.View.OnError))

            it("hides the Progress") {
                verify(layout).showError()
            }
        }
    }
})
