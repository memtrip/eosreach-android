package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.app.Application
import com.memtrip.eosreach.api.bandwidth.GetBandwidthError
import com.memtrip.eosreach.api.bandwidth.GetBandwidthRequest
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class DelegateBandwidthListViewModel @Inject internal constructor(
    private val getBandwidthRequest: GetBandwidthRequest,
    application: Application
) : MxViewModel<DelegateBandwidthListIntent, DelegateListRenderAction, DelegateBandwidthListViewState>(
    DelegateBandwidthListViewState(view = DelegateBandwidthListViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: DelegateBandwidthListIntent): Observable<DelegateListRenderAction> = when (intent) {
        is DelegateBandwidthListIntent.Init -> getDelegatedBandwidth(intent.accountName)
        DelegateBandwidthListIntent.Idle -> Observable.just(DelegateListRenderAction.Idle)
        is DelegateBandwidthListIntent.NavigateToUndelegateBandwidth ->
            Observable.just(DelegateListRenderAction.NavigateToUndelegateBandwidth(intent.delegatedBandwidth))
    }

    override fun reducer(previousState: DelegateBandwidthListViewState, renderAction: DelegateListRenderAction): DelegateBandwidthListViewState = when (renderAction) {
        DelegateListRenderAction.Idle ->
            previousState.copy(view = DelegateBandwidthListViewState.View.Idle)
        DelegateListRenderAction.OnProgress ->
            previousState.copy(view = DelegateBandwidthListViewState.View.OnProgress)
        DelegateListRenderAction.OnError ->
            previousState.copy(view = DelegateBandwidthListViewState.View.OnError)
        is DelegateListRenderAction.Populate ->
            previousState.copy(view = DelegateBandwidthListViewState.View.Populate(renderAction.bandwidth))
        is DelegateListRenderAction.NavigateToUndelegateBandwidth ->
            previousState.copy(view = DelegateBandwidthListViewState.View.NavigateToUndelegateBandwidth(renderAction.delegatedBandwidth))
        DelegateListRenderAction.Empty ->
            previousState.copy(view = DelegateBandwidthListViewState.View.Empty)
    }

    private fun getDelegatedBandwidth(accountName: String): Observable<DelegateListRenderAction> {
        return getBandwidthRequest.getBandwidth(accountName).map { result ->
            if (result.success) {
                DelegateListRenderAction.Populate(result.data!!)
            } else {
                delegatedBandwidthError(result.apiError!!)
            }
        }.toObservable().startWith(DelegateListRenderAction.OnProgress)
    }

    private fun delegatedBandwidthError(getBandwidthError: GetBandwidthError): DelegateListRenderAction = when (getBandwidthError) {
        GetBandwidthError.GenericError -> DelegateListRenderAction.OnError
        GetBandwidthError.Empty -> DelegateListRenderAction.Empty
    }
}