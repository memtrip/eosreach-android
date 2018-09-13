package com.memtrip.eosreach.app.transfer.form

import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class TransferFormRenderAction : MxRenderAction {
    object Idle : TransferFormRenderAction()
    data class Populate(val contractAccountBalance: ContractAccountBalance) : TransferFormRenderAction()
    data class OnComplete(val transferFormData: TransferFormData) : TransferFormRenderAction()
    data class OnValidationError(val message: String) : TransferFormRenderAction()
}

interface TransferFormViewLayout : MxViewLayout {
    fun populate(contractAccountBalance: ContractAccountBalance)
    fun showValidationError(message: String)
    fun navigateToConfirmation(transferFormData: TransferFormData)
}

class TransferFormViewRenderer @Inject internal constructor() : MxViewRenderer<TransferFormViewLayout, TransferFormViewState> {
    override fun layout(layout: TransferFormViewLayout, state: TransferFormViewState): Unit = when (state.view) {
        TransferFormViewState.View.Idle -> {
        }
        is TransferFormViewState.View.Populate -> {
            layout.populate(state.view.contractAccountBalance)
        }
        is TransferFormViewState.View.OnValidationError -> {
            layout.showValidationError(state.view.message)
        }
        is TransferFormViewState.View.OnComplete -> {
            layout.navigateToConfirmation(state.view.transferFormData)
        }
    }
}