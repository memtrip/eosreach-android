package com.memtrip.eosreach.app.blockproducerlist

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.facebook.drawee.drawable.ScalingUtils

import com.memtrip.eosreach.R

import com.memtrip.eosreach.db.blockproducer.BlockProducerEntity

import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.SimpleAdapter
import com.memtrip.eosreach.uikit.SimpleAdapterViewHolder
import io.reactivex.subjects.PublishSubject

import kotlinx.android.synthetic.main.block_producer_list_item.view.*

class BlockProducerListAdapter(
    context: Context,
    interaction: PublishSubject<Interaction<BlockProducerEntity>>
) : SimpleAdapter<BlockProducerEntity>(context, interaction) {

    override fun createViewHolder(parent: ViewGroup): SimpleAdapterViewHolder<BlockProducerEntity> {
        return BlockProducerViewHolder(
            inflater.inflate(R.layout.block_producer_list_item, parent, false))
    }
}

class BlockProducerViewHolder(itemView: View) : SimpleAdapterViewHolder<BlockProducerEntity>(itemView) {

    override fun populate(position: Int, value: BlockProducerEntity) {
        itemView.block_producer_list_item_account_name.text = value.accountName
        itemView.block_producer_list_item_candidate_name.text = value.candidateName
        itemView.block_producer_list_item_logo.hierarchy.actualImageScaleType = ScalingUtils.ScaleType.CENTER_CROP
        itemView.block_producer_list_item_logo.setImageURI(value.logoUrl)
    }
}