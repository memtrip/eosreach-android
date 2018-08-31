package com.memtrip.eosreach.app.account.resources

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class ResourcesRendererTest : Spek({

    val layout by memoized { mock<ResourcesViewLayout>() }

    given("a ResourcesRenderer") {

        val renderer by memoized { ResourcesViewRenderer() }

        on("ViewState is not initialized") {
            renderer.layout(layout, ResourcesViewState(ResourcesViewState.View.OnProgress))

            it("shows the Progress") {
                verify(layout).showProgress()
            }
        }

        on("ViewState is initialized") {
            renderer.layout(layout, ResourcesViewState(ResourcesViewState.View.OnError))

            it("hides the Progress") {
                verify(layout).showError()
            }
        }
    }
})
