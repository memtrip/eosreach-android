/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
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
    fun populate(formattedBalance: String)
    fun showValidationError(message: String)
    fun navigateToConfirmation(transferFormData: TransferFormData)
}

class TransferFormViewRenderer @Inject internal constructor() : MxViewRenderer<TransferFormViewLayout, TransferFormViewState> {
    override fun layout(layout: TransferFormViewLayout, state: TransferFormViewState): Unit = when (state.view) {
        TransferFormViewState.View.Idle -> {
        }
        is TransferFormViewState.View.Populate -> {
            val contractAccountBalance = state.view.contractAccountBalance
            layout.populate(
                "${contractAccountBalance.balance.amount} ${contractAccountBalance.balance.symbol}")
        }
        is TransferFormViewState.View.OnValidationError -> {
            layout.showValidationError(state.view.message)
        }
        is TransferFormViewState.View.OnComplete -> {
            layout.navigateToConfirmation(state.view.transferFormData)
        }
    }
}