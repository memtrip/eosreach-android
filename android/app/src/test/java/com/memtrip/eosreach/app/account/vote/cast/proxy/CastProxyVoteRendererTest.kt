package com.memtrip.eosreach.app.account.vote.cast.proxy

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class CastProxyVoteRendererTest : Spek({

    val layout by memoized { mock<CastProxyVoteViewLayout>() }

    given("a CastProxyVoteRenderer") {

        val renderer by memoized { CastProxyVoteViewRenderer() }

        on("ViewState is not initialized") {
            renderer.layout(layout, CastProxyVoteViewState(CastProxyVoteViewState.View.OnProgress))

            it("shows the Progress") {
                verify(layout).showProgress()
            }
        }

        on("ViewState is initialized") {
            renderer.layout(layout, CastProxyVoteViewState(CastProxyVoteViewState.View.OnError))

            it("hides the Progress") {
                verify(layout).showError()
            }
        }
    }
})
