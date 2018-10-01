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

import android.app.Application
import com.memtrip.eosreach.R
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class TransferFormViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<TransferFormIntent, TransferFormRenderAction, TransferFormViewState>(
    TransferFormViewState(view = TransferFormViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: TransferFormIntent): Observable<TransferFormRenderAction> = when (intent) {
        TransferFormIntent.Idle -> Observable.just(TransferFormRenderAction.Idle)
        is TransferFormIntent.Init -> Observable.just(TransferFormRenderAction.Populate(intent.contractAccountBalance))
        is TransferFormIntent.SubmitForm -> validateForm(intent.transferFormData)
    }

    override fun reducer(previousState: TransferFormViewState, renderAction: TransferFormRenderAction): TransferFormViewState = when (renderAction) {
        TransferFormRenderAction.Idle -> previousState.copy(view = TransferFormViewState.View.Idle)
        is TransferFormRenderAction.Populate -> previousState.copy(
            view = TransferFormViewState.View.Populate(renderAction.contractAccountBalance))
        is TransferFormRenderAction.OnValidationError -> previousState.copy(
            view = TransferFormViewState.View.OnValidationError(renderAction.message))
        is TransferFormRenderAction.OnComplete -> previousState.copy(
            view = TransferFormViewState.View.OnComplete(renderAction.transferFormData)
        )
    }

    private fun validateForm(transferFormData: TransferFormData): Observable<TransferFormRenderAction> {
        if (transferFormData.amount.isEmpty()) {
            return Observable.just(TransferFormRenderAction.OnValidationError(
                context().getString(R.string.transfer_form_account_validation)
            ))
        } else if (transferFormData.amount.isEmpty()) {
            return Observable.just(TransferFormRenderAction.OnValidationError(
                context().getString(R.string.transfer_form_amount_validation)
            ))
        } else {
            return Observable.just(TransferFormRenderAction.OnComplete(transferFormData))
        }
    }
}