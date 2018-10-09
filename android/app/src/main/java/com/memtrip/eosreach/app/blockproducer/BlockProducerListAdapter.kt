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
package com.memtrip.eosreach.app.blockproducer

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.drawable.ScalingUtils
import com.jakewharton.rxbinding2.view.RxView

import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.blockproducer.BlockProducerDetails

import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.SimpleAdapter
import com.memtrip.eosreach.uikit.SimpleAdapterViewHolder
import io.reactivex.subjects.PublishSubject

import kotlinx.android.synthetic.main.block_producer_list_item_row.view.*

class BlockProducerListAdapter(
    context: Context,
    interaction: PublishSubject<Interaction<BlockProducerDetails>>
) : SimpleAdapter<BlockProducerDetails>(context, interaction) {

    override fun createViewHolder(parent: ViewGroup): SimpleAdapterViewHolder<BlockProducerDetails> {
        val viewHolder = BlockProducerViewHolder(
            inflater.inflate(R.layout.block_producer_list_item_row, parent, false))

        RxView.clicks(viewHolder.itemView.block_producer_list_item_information).map {
            Interaction(R.id.block_producer_list_item_information, data[viewHolder.adapterPosition])
        }.subscribe(interaction)

        return viewHolder
    }

    override fun defaultRowMargin(position: Int, view: View) {
        if (position == data.size - 1) {
            (view.layoutParams as RecyclerView.LayoutParams).bottomMargin = marginSize
        } else {
            (view.layoutParams as RecyclerView.LayoutParams).bottomMargin = 0
        }
    }
}

class BlockProducerViewHolder(itemView: View) : SimpleAdapterViewHolder<BlockProducerDetails>(itemView) {

    override fun populate(position: Int, value: BlockProducerDetails) {
        itemView.block_producer_list_item_account_name.text = value.owner
        itemView.block_producer_list_item_candidate_name.text = value.candidateName
        itemView.block_producer_list_item_logo.hierarchy.actualImageScaleType = ScalingUtils.ScaleType.CENTER_CROP
        value.logo256?.let { logo -> itemView.block_producer_list_item_logo.setImageURI(logo) }
    }
}