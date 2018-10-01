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
package com.memtrip.eosreach.app.transfer.confirm

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout

import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.uikit.BalanceDetailsLayout
import com.memtrip.eosreach.uikit.BalanceDetailsLayoutImpl
import com.memtrip.eosreach.uikit.visible

import kotlinx.android.synthetic.main.transfer_details_layout.view.*

class TransferDetailLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), BalanceDetailsLayout by BalanceDetailsLayoutImpl() {

    init {
        LayoutInflater.from(context).inflate(R.layout.transfer_details_layout, this)
    }

    fun populate(
        transferAmount: Balance,
        to: String,
        from: String,
        memo: String,
        contractAccountBalance: ContractAccountBalance
    ) {
        populateBasicDetails(to, from, memo)

        transfer_details_amount_value.text = formatBalance(
            transferAmount, contractAccountBalance, context)
    }

    fun populateDate(formattedDateString: String) {
        transfer_details_date_group.visible()
        transfer_details_date_value.text = formattedDateString
    }

    private fun populateBasicDetails(
        to: String,
        from: String,
        memo: String
    ) {
        transfer_details_to_value.text = to
        transfer_details_from_value.text = from
        transfer_details_memo_value.text = memo
    }
}