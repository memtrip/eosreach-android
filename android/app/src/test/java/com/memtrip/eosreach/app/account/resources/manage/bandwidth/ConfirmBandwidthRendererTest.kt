package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class ConfirmBandwidthRendererTest : Spek({

    val layout by memoized { mock<BandwidthConfirmViewLayout>() }

    given("a ConfirmBandwidthRenderer") {

        val renderer by memoized { BandwidthConfirmViewRenderer() }

        on("ViewState is not initialized") {
            renderer.layout(layout, BandwidthConfirmViewState(BandwidthConfirmViewState.View.OnProgress))

            it("shows the Progress") {
                verify(layout).showProgress()
            }
        }

        on("ViewState is initialized") {
            renderer.layout(layout, BandwidthConfirmViewState(BandwidthConfirmViewState.View.OnError))

            it("hides the Progress") {
                verify(layout).showError()
            }
        }
    }
})
