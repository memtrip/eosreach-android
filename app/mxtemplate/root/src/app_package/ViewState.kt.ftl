package ${packageName}

import com.memtrip.mxandroid.MxViewState

data class ${className}ViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        object OnError : View()
    }
}