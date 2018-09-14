package com.memtrip.eosreach.app.account.resources.manage.manageram

import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class ManageRamRenderAction : MxRenderAction {
    object BuyRamTabIdle : ManageRamRenderAction()
    object SellRamTabIdle : ManageRamRenderAction()
    data class Init(val eosAccount: EosAccount) : ManageRamRenderAction()
}

interface ManageRamViewLayout : MxViewLayout {
    fun populate(eosAccount: EosAccount, page: ManageRamFragmentPagerAdapter.Page)
}

class ManageRamViewRenderer @Inject internal constructor() : MxViewRenderer<ManageRamViewLayout, ManageRamViewState> {
    override fun layout(layout: ManageRamViewLayout, state: ManageRamViewState): Unit = when (state.view) {
        ManageRamViewState.View.Idle -> {
        }
        is ManageRamViewState.View.Populate -> {
            layout.populate(state.view.eosAccount, state.page)
        }
    }
}