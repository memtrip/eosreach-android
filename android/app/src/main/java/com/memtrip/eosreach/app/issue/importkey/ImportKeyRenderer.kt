package com.memtrip.eosreach.app.issue.importkey

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class ImportKeyRenderAction : MxRenderAction {
    object Idle : ImportKeyRenderAction()
    object OnProgress : ImportKeyRenderAction()
    object OnSuccess : ImportKeyRenderAction()
    data class OnError(val error: String) : ImportKeyRenderAction()
    object NavigateToGithubSource : ImportKeyRenderAction()
    object NavigateToSettings : ImportKeyRenderAction()
}

interface ImportKeyViewLayout : MxViewLayout {
    fun showDefaults()
    fun showProgress()
    fun showError(error: String)
    fun success()
    fun navigateToGithubSource()
    fun navigateToSettings()
}

class ImportKeyViewRenderer @Inject internal constructor() : MxViewRenderer<ImportKeyViewLayout, ImportKeyViewState> {
    override fun layout(layout: ImportKeyViewLayout, state: ImportKeyViewState): Unit = when (state.view) {
        ImportKeyViewState.View.Idle -> {
            layout.showDefaults()
        }
        ImportKeyViewState.View.OnProgress -> {
            layout.showProgress()
        }
        ImportKeyViewState.View.OnSuccess -> {
            layout.success()
        }
        is ImportKeyViewState.View.OnError -> {
            layout.showError(state.view.error)
        }
        ImportKeyViewState.View.NavigateToGithubSource -> {
            layout.navigateToGithubSource()
        }
        ImportKeyViewState.View.NavigateToSettings -> {
            layout.navigateToSettings()
        }
    }
}