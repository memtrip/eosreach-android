package com.memtrip.eosreach.app.account.resources.manage

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class ManageRamRendererTest : Spek({

    val layout by memoized { mock<ManageRamViewLayout>() }

    given("a ManageRamRenderer") {

        val renderer by memoized { ManageRamViewRenderer() }

        on("ViewState is not initialized") {
            renderer.layout(layout, ManageRamViewState(ManageRamViewState.View.OnProgress))

            it("shows the Progress") {
                verify(layout).showProgress()
            }
        }

        on("ViewState is initialized") {
            renderer.layout(layout, ManageRamViewState(ManageRamViewState.View.OnError))

            it("hides the Progress") {
                verify(layout).showError()
            }
        }
    }
})
