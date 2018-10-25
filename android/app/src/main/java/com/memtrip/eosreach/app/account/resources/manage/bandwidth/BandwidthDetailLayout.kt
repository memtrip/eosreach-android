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
package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.uikit.BalanceDetailsLayout
import com.memtrip.eosreach.uikit.BalanceDetailsLayoutImpl
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible
import kotlinx.android.synthetic.main.bandwidth_details_layout.view.*

class BandwidthDetailLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), BalanceDetailsLayout by BalanceDetailsLayoutImpl() {

    init {
        LayoutInflater.from(context).inflate(R.layout.bandwidth_details_layout, this)
    }

    fun populate(
        targetAccount: String,
        cpu: Balance,
        net: Balance,
        transfer: Boolean,
        contractAccountBalance: ContractAccountBalance
    ) {
        bandwidth_details_cpu_value.text = formatBalance(cpu, contractAccountBalance, context)
        bandwidth_details_net_value.text = formatBalance(net, contractAccountBalance, context)
        bandwidth_details_target_account_value.text = targetAccount

        if (transfer) {
            bandwidth_details_transfer_funds_label.visible()
        } else {
            bandwidth_details_transfer_funds_label.gone()
        }
    }
}