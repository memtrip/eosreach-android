package com.memtrip.eosreach.app.account.resources.manage

import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthManageViewLayout
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.ManageBandwidthViewRenderer
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthManageViewState
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class ManageBandwidthRendererTest : Spek({

    val layout by memoized { mock<BandwidthManageViewLayout>() }

    given("a ManageBandwidthRenderer") {

        val renderer by memoized { ManageBandwidthViewRenderer() }

        on("ViewState is not initialized") {
            renderer.layout(layout, BandwidthManageViewState(BandwidthManageViewState.View.OnProgress))

            it("shows the Progress") {
                verify(layout).showProgress()
            }
        }

        on("ViewState is initialized") {
            renderer.layout(layout, BandwidthManageViewState(BandwidthManageViewState.View.OnError))

            it("hides the Progress") {
                verify(layout).showError()
            }
        }
    }
})
