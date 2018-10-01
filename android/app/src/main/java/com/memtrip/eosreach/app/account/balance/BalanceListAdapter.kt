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
package com.memtrip.eosreach.app.account.balance

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.SimpleAdapter
import com.memtrip.eosreach.uikit.SimpleAdapterViewHolder
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.account_balance_list_item.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

class AccountBalanceListAdapter(
    context: Context,
    interaction: PublishSubject<Interaction<ContractAccountBalance>>
) : SimpleAdapter<ContractAccountBalance>(context, interaction) {

    override fun createViewHolder(parent: ViewGroup): SimpleAdapterViewHolder<ContractAccountBalance> {
        return AccountBalanceViewHolder(
            inflater.inflate(R.layout.account_balance_list_item, parent, false))
    }
}

class AccountBalanceViewHolder(itemView: View) : SimpleAdapterViewHolder<ContractAccountBalance>(itemView) {

    override fun populate(position: Int, value: ContractAccountBalance) {
        itemView.welcome_accounts_list_item_account_name.text = value.balance.symbol
        itemView.welcome_accounts_list_item_balance.text = with (DecimalFormat("0.0000")) {
            roundingMode = RoundingMode.CEILING
            this
        }.format(value.balance.amount)
    }
}