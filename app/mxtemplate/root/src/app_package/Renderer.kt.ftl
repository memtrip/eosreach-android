package ${packageName}

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class ${className}RenderAction : MxRenderAction {
    object OnProgress : ${className}RenderAction()
    object OnError : ${className}RenderAction()
}

interface ${className}ViewLayout : MxViewLayout {
    fun showProgress()
    fun showError()
}

class ${className}ViewRenderer @Inject internal constructor() : MxViewRenderer<${className}ViewLayout, ${className}ViewState> {
    override fun layout(layout: ${className}ViewLayout, state: ${className}ViewState): Unit = when (state.view) {
        ${className}ViewState.View.Idle -> {

        }
        ${className}ViewState.View.OnProgress -> {
            layout.showProgress()
        }
        ${className}ViewState.View.OnError -> {
            layout.showError()
        }
    }
}