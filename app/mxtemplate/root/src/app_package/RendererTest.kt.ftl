package ${packageName}

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class ${className}RendererTest : Spek({

    val layout by memoized { mock<${className}ViewLayout>() }

    given("a ${className}Renderer") {

        val renderer by memoized { ${className}ViewRenderer() }

        on("ViewState is not initialized") {
            renderer.layout(layout, ${className}ViewState(${className}ViewState.View.OnProgress))

            it("shows the Progress") {
                verify(layout).showProgress()
            }
        }

        on("ViewState is initialized") {
            renderer.layout(layout, ${className}ViewState(${className}ViewState.View.OnError))

            it("hides the Progress") {
                verify(layout).showError()
            }
        }
    }
})
