package com.memtrip.eosreach.app.account.resources.manage.bandwidth.form

import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthFormViewLayout
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthFormViewRenderer
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthFormViewState
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class ManageBandwidthFormRendererTest : Spek({

    val layout by memoized { mock<BandwidthFormViewLayout>() }

    given("a ManageBandwidthFormRenderer") {

        val renderer by memoized { BandwidthFormViewRenderer() }

        on("ViewState is not initialized") {
            renderer.layout(layout, BandwidthFormViewState(BandwidthFormViewState.View.OnProgress))

            it("shows the Progress") {
                verify(layout).showProgress()
            }
        }

        on("ViewState is initialized") {
            renderer.layout(layout, BandwidthFormViewState(BandwidthFormViewState.View.OnError))

            it("hides the Progress") {
                verify(layout).showError()
            }
        }
    }
})
