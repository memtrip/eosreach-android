package com.memtrip.eosreach.app.account.resources.manage.manageram

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class RamFormRenderAction : MxRenderAction {
    object Idle : RamFormRenderAction()
    data class NavigateToConfirmRamForm(
        val kilobytes: String,
        val ramCommitType: RamCommitType
    ) : RamFormRenderAction()
    data class UpdateCostPerKiloByte(val eosCost: String) : RamFormRenderAction()
    object EmptyRamError : RamFormRenderAction()
}

interface RamFormViewLayout : MxViewLayout {
    fun updateEosCost(eosCost: String)
    fun navigateToConfirmRamForm(kilobytes: String, ramCommitType: RamCommitType)
    fun emptyRamError()
}

class RamFormViewRenderer @Inject internal constructor() : MxViewRenderer<RamFormViewLayout, RamFormViewState> {
    override fun layout(layout: RamFormViewLayout, state: RamFormViewState): Unit = when (state.view) {
        RamFormViewState.View.Idle -> {
        }
        is RamFormViewState.View.UpdateCostPerKiloByte -> {
            layout.updateEosCost(state.view.eosCost)
        }
        is RamFormViewState.View.NavigateToConfirmRamForm -> {
            layout.navigateToConfirmRamForm(state.view.kilobytes, state.view.ramCommitType)
        }
        RamFormViewState.View.EmptyRamError -> {
            layout.emptyRamError()
        }
    }
}