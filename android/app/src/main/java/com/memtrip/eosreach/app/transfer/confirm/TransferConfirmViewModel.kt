package com.memtrip.eosreach.app.transfer.confirm

import android.app.Application
import com.memtrip.eosreach.R

import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class TransferConfirmViewModel @Inject internal constructor(
    private val transferUseCase: TransferUseCase,
    application: Application
) : MxViewModel<TransferConfirmIntent, TransferConfirmRenderAction, TransferConfirmViewState>(
    TransferConfirmViewState(view = TransferConfirmViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: TransferConfirmIntent): Observable<TransferConfirmRenderAction> = when (intent) {
        TransferConfirmIntent.Idle -> Observable.just(TransferConfirmRenderAction.Idle)
        is TransferConfirmIntent.Init -> Observable.just(TransferConfirmRenderAction.Populate(intent.transferFormData))
        is TransferConfirmIntent.Transfer -> transfer(intent.transferRequestData)
        is TransferConfirmIntent.ViewLog -> Observable.just(TransferConfirmRenderAction.ViewLog(intent.log))
    }

    override fun reducer(previousState: TransferConfirmViewState, renderAction: TransferConfirmRenderAction): TransferConfirmViewState = when (renderAction) {
        TransferConfirmRenderAction.Idle -> previousState.copy(
            view = TransferConfirmViewState.View.Idle)
        is TransferConfirmRenderAction.Populate -> previousState.copy(
            view = TransferConfirmViewState.View.Populate(renderAction.transferFormData))
        TransferConfirmRenderAction.OnProgress -> previousState.copy(
            view = TransferConfirmViewState.View.OnProgress)
        is TransferConfirmRenderAction.OnError -> previousState.copy(
            view = TransferConfirmViewState.View.OnError(renderAction.message, renderAction.log))
        is TransferConfirmRenderAction.OnSuccess -> previousState.copy(
            view = TransferConfirmViewState.View.OnSuccess(renderAction.transferReceipt))
        is TransferConfirmRenderAction.ViewLog -> previousState.copy(
            view = TransferConfirmViewState.View.ViewLog(renderAction.log))
    }

    override fun filterIntents(intents: Observable<TransferConfirmIntent>): Observable<TransferConfirmIntent> = Observable.merge(
        intents.ofType(TransferConfirmIntent.Init::class.java).take(1),
        intents.filter {
            !TransferConfirmIntent.Init::class.java.isInstance(it)
        }
    )

    private fun transfer(transferRequestData: TransferRequestData): Observable<TransferConfirmRenderAction> {
        return transferUseCase.transfer(
            transferRequestData.fromAccount,
            transferRequestData.toAccount,
            transferRequestData.quantity,
            transferRequestData.memo
        ).map { result ->
            if (result.success) {
                TransferConfirmRenderAction.OnSuccess(result.data!!)
            } else {
                TransferConfirmRenderAction.OnError(
                    context().getString(R.string.transfer_confirm_error_message),
                    result.apiError!!.body
                )
            }
        }.toObservable().startWith(TransferConfirmRenderAction.OnProgress)
    }
}