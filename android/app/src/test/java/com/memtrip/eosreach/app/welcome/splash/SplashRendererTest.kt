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

        on("Navigate to create account") {
            renderer.layout(layout, SplashViewState(SplashViewState.View.NavigateToCreateAccount))

            it("navigates to create account") {
                verify(layout).navigateToCreateAccount()
            }
        }

        on("Navigate to import key") {
            renderer.layout(layout, SplashViewState(SplashViewState.View.NavigateToImportKeys))

            it("navigates to import key") {
                verify(layout).navigateToImportKey()
            }
        }
    }
})
