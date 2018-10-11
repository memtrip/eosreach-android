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
package com.memtrip.eosreach.app.transaction.log

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.db.transaction.TransactionLogEntity
import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.SimpleAdapter
import com.memtrip.eosreach.uikit.SimpleAdapterViewHolder
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.transaction_view_confirmed_list_item.view.*

class ViewConfirmedTransactionsAdapter(
    context: Context,
    interaction: PublishSubject<Interaction<TransactionLogEntity>>
) : SimpleAdapter<TransactionLogEntity>(context, interaction) {

    override fun createViewHolder(parent: ViewGroup): SimpleAdapterViewHolder<TransactionLogEntity> {
        return ViewConfirmedTransactionsViewHolder(
            inflater.inflate(R.layout.transaction_view_confirmed_list_item, parent, false))
    }

    override fun defaultRowMargin(position: Int, view: View) {
        if (position == data.size - 1) {
            (view.layoutParams as RecyclerView.LayoutParams).bottomMargin = marginSize
        } else {
            (view.layoutParams as RecyclerView.LayoutParams).bottomMargin = 0
        }
    }
}

class ViewConfirmedTransactionsViewHolder(itemView: View) : SimpleAdapterViewHolder<TransactionLogEntity>(itemView) {

    override fun populate(position: Int, value: TransactionLogEntity) {
        itemView.transaction_view_confirmed_list_item_id.text = value.transactionId
        itemView.transaction_view_confirmed_list_item_date.text = value.formattedDate
    }
}