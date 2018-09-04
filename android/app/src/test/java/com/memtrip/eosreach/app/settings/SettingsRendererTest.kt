package com.memtrip.eosreach.app.settings

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class SettingsRendererTest : Spek({

    val layout by memoized { mock<SettingsViewLayout>() }

    given("a SettingsRenderer") {

        val renderer by memoized { SettingsViewRenderer() }

        on("ViewState is not initialized") {
            renderer.layout(layout, SettingsViewState(SettingsViewState.View.OnProgress))

            it("shows the Progress") {
                verify(layout).showProgress()
            }
        }

        on("ViewState is initialized") {
            renderer.layout(layout, SettingsViewState(SettingsViewState.View.OnError))

            it("hides the Progress") {
                verify(layout).showError()
            }
        }
    }
})
