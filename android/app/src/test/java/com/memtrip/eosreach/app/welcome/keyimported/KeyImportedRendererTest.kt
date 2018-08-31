package com.memtrip.eosreach.app.welcome.keyimported

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class KeyImportedRendererTest : Spek({

    val layout by memoized { mock<KeyImportedViewLayout>() }

    given("a KeyImportedRenderer") {

        val renderer by memoized { KeyImportedViewRenderer() }

        on("ViewState is not initialized") {
            renderer.layout(layout, KeyImportedViewState(KeyImportedViewState.View.Done))

            it("shows the Progress") {
                verify(layout).showProgress()
            }
        }

        on("ViewState is initialized") {
            renderer.layout(layout, KeyImportedViewState(KeyImportedViewState.View.CopyToClipboard))

            it("hides the Progress") {
                verify(layout).showError()
            }
        }
    }
})
