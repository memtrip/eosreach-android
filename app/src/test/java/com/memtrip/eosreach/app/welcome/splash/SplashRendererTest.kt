package com.memtrip.eosreach.app.welcome.splash

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class SplashRendererTest : Spek({

    val layout by memoized { mock<SplashViewLayout>() }

    given("a SplashRenderer") {

        val renderer by memoized { SplashViewRenderer() }

        on("ViewState is not initialized") {
            renderer.layout(layout, SplashViewState(SplashViewState.View.OnProgress))

            it("shows the Progress") {
                verify(layout).showProgress()
            }
        }

        on("ViewState is initialized") {
            renderer.layout(layout, SplashViewState(SplashViewState.View.OnError))

            it("hides the Progress") {
                verify(layout).showError()
            }
        }
    }
})
