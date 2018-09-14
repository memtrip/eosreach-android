package com.memtrip.eosreach.app.account.resources.manage

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class ManageRamRenderAction : MxRenderAction {
    object OnProgress : ManageRamRenderAction()
    object OnError : ManageRamRenderAction()
}

interface ManageRamViewLayout : MxViewLayout {
    fun showProgress()
    fun showError()
}

class ManageRamViewRenderer @Inject internal constructor() : MxViewRenderer<ManageRamViewLayout, ManageRamViewState> {
    override fun layout(layout: ManageRamViewLayout, state: ManageRamViewState): Unit = when (state.view) {
        ManageRamViewState.View.Idle -> {

        }
        ManageRamViewState.View.OnProgress -> {
            layout.showProgress()
        }
        ManageRamViewState.View.OnError -> {
            layout.showError()
        }
    }
}