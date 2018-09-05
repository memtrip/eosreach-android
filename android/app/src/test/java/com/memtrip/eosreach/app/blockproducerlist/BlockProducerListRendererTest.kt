package com.memtrip.eosreach.app.blockproducerlist

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class BlockProducerListRendererTest : Spek({

    val layout by memoized { mock<BlockProducerListViewLayout>() }

    given("a BlockProducerListRenderer") {

        val renderer by memoized { BlockProducerListViewRenderer() }

        on("ViewState is not initialized") {
            renderer.layout(layout, BlockProducerListViewState(BlockProducerListViewState.View.OnProgress))

            it("shows the Progress") {
                verify(layout).showProgress()
            }
        }

        on("ViewState is initialized") {
            renderer.layout(layout, BlockProducerListViewState(BlockProducerListViewState.View.OnError))

            it("hides the Progress") {
                verify(layout).showError()
            }
        }
    }
})
