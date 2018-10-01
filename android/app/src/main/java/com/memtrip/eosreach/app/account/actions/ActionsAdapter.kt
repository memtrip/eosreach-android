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
package com.memtrip.eosreach.app.account.actions

import android.content.Context
import android.view.View
import android.view.ViewGroup

import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.actions.model.AccountAction

import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.SimpleAdapter
import com.memtrip.eosreach.uikit.SimpleAdapterViewHolder

import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.account_actions_list_item.view.*

class AccountActionsAdapter(
    context: Context,
    interaction: PublishSubject<Interaction<AccountAction>>
) : SimpleAdapter<AccountAction>(context, interaction) {

    override fun createViewHolder(parent: ViewGroup): SimpleAdapterViewHolder<AccountAction> {
        return AccountActionsViewHolder(
            inflater.inflate(R.layout.account_actions_list_item, parent, false))
    }
}

class AccountActionsViewHolder(itemView: View) : SimpleAdapterViewHolder<AccountAction>(itemView) {

    override fun populate(position: Int, value: AccountAction) {
        when (value) {
            is AccountAction.Transfer -> {
                itemView.account_actions_list_item_interaction_account_name.text = value.transferInteractingAccountName
                itemView.account_actions_list_item_icon.setImageDrawable(itemView.context.getDrawable(value.transferIncomingIcon))
                itemView.account_actions_list_item_interaction_crypto_value.text = value.quantityString
                itemView.account_actions_list_item_interaction_fiat_value.text = value.currencyPairValue
                itemView.account_actions_list_item_date_created.text = value.formattedDate
            }
        }
    }
}