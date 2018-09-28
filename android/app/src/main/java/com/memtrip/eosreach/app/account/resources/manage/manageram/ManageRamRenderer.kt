package com.memtrip.eosreach.app.account.resources.manage.manageram

import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

sealed class ManageRamRenderAction : MxRenderAction {
    object BuyRamTabIdle : ManageRamRenderAction()
    object SellRamTabIdle : ManageRamRenderAction()
    object OnProgress : ManageRamRenderAction()
    object OnRamPriceError : ManageRamRenderAction()
    data class Populate(val ramPricePerKb: Balance) : ManageRamRenderAction()
}

interface ManageRamViewLayout : MxViewLayout {
    fun populate(
        ramPricePerKb: Balance,
        formattedRamPrice: String,
        page: ManageRamFragmentPagerAdapter.Page
    )
    fun showProgress()
    fun showRamPriceError()
}

class ManageRamViewRenderer @Inject internal constructor() : MxViewRenderer<ManageRamViewLayout, ManageRamViewState> {
    override fun layout(layout: ManageRamViewLayout, state: ManageRamViewState): Unit = when (state.view) {
        ManageRamViewState.View.Idle -> {
        }
        is ManageRamViewState.View.Populate -> {
            val formattedRamPrice = with (DecimalFormat("0.00000000")) {
                roundingMode = RoundingMode.CEILING
                this
            }.format(state.view.ramPrice.amount)

            layout.populate(state.view.ramPrice, formattedRamPrice, state.page)
        }
        ManageRamViewState.View.OnProgress -> {
            layout.showProgress()
        }
        ManageRamViewState.View.OnRamPriceError -> {
            layout.showRamPriceError()
        }
    }
}