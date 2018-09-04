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
        is TransferFormIntent.Init -> Observable.just(TransferFormRenderAction.Idle)
        is TransferFormIntent.SubmitForm -> validateForm(intent.transferFormData)
    }

    override fun reducer(previousState: TransferFormViewState, renderAction: TransferFormRenderAction): TransferFormViewState = when (renderAction) {
        TransferFormRenderAction.Idle -> previousState.copy(view = TransferFormViewState.View.Idle)
        is TransferFormRenderAction.OnValidationError -> previousState.copy(
            view = TransferFormViewState.View.OnValidationError(renderAction.message))
        is TransferFormRenderAction.OnComplete -> previousState.copy(
            view = TransferFormViewState.View.OnComplete(renderAction.transferFormData)
        )
    }

    private fun validateForm(transferFormData: TransferFormData) : Observable<TransferFormRenderAction> {
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