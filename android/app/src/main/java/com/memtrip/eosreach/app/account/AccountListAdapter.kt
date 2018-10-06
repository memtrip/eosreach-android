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
package com.memtrip.eosreach.app.account

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView

import com.memtrip.eosreach.R
import com.memtrip.eosreach.db.account.AccountEntity

import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.SimpleAdapter
import com.memtrip.eosreach.uikit.SimpleAdapterViewHolder
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.account_list_item.view.*
import kotlinx.android.synthetic.main.account_list_item_row.view.*

class AccountListAdapter(
    context: Context,
    interaction: PublishSubject<Interaction<AccountEntity>>
) : SimpleAdapter<AccountEntity>(context, interaction) {

    override fun createViewHolder(parent: ViewGroup): SimpleAdapterViewHolder<AccountEntity> {
        val viewHolder = AccountViewHolder(
            inflater.inflate(R.layout.account_list_item_row, parent, false))

        RxView.clicks(viewHolder.itemView.account_list_item.accounts_list_item_container).map {
            Interaction(viewHolder.itemView.account_list_item.accounts_list_item_container.id, data[viewHolder.adapterPosition])
        }.subscribe(interaction)

        return viewHolder
    }
}

class AccountViewHolder(itemView: View) : SimpleAdapterViewHolder<AccountEntity>(itemView) {

    override fun populate(position: Int, value: AccountEntity) {
        itemView.account_list_item.populate(value)
    }
}