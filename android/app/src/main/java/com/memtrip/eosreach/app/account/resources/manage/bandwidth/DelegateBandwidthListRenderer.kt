package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import com.memtrip.eosreach.api.bandwidth.DelegatedBandwidth
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class DelegateListRenderAction : MxRenderAction {
    object Idle : DelegateListRenderAction()
    object OnProgress : DelegateListRenderAction()
    data class Populate(
        val bandwidth: List<DelegatedBandwidth>
    ) : DelegateListRenderAction()
    object OnError : DelegateListRenderAction()
    object Empty : DelegateListRenderAction()
    data class NavigateToUndelegateBandwidth(
        val delegatedBandwidth: DelegatedBandwidth
    ) : DelegateListRenderAction()
}

interface DelegateListViewLayout : MxViewLayout {
    fun showProgress()
    fun showError()
    fun populate(delegatedBandwidth: List<DelegatedBandwidth>)
    fun showEmptyBandwidth()
    fun navigateToUndelegateBandwidth(delegatedBandwidth: DelegatedBandwidth)
}

class DelegateListViewRenderer @Inject internal constructor() : MxViewRenderer<DelegateListViewLayout, DelegateBandwidthListViewState> {
    override fun layout(layout: DelegateListViewLayout, state: DelegateBandwidthListViewState): Unit = when (state.view) {
        DelegateBandwidthListViewState.View.Idle -> {
        }
        DelegateBandwidthListViewState.View.OnProgress -> {
            layout.showProgress()
        }
        DelegateBandwidthListViewState.View.OnError -> {
            layout.showError()
        }
        is DelegateBandwidthListViewState.View.Populate -> {
            layout.populate(state.view.bandwidth)
        }
        is DelegateBandwidthListViewState.View.NavigateToUndelegateBandwidth -> {
            layout.navigateToUndelegateBandwidth(state.view.delegatedBandwidth)
        }
        DelegateBandwidthListViewState.View.Empty -> {
            layout.showEmptyBandwidth()
        }
    }
}