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
            val formattedRamPrice = with(DecimalFormat("0.0000")) {
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