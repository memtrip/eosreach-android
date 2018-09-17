package com.memtrip.eosreach.app.account.vote

import android.content.Context
import android.view.View
import android.view.ViewGroup

import com.memtrip.eosreach.R

import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.SimpleAdapter
import com.memtrip.eosreach.uikit.SimpleAdapterViewHolder
import io.reactivex.subjects.PublishSubject

import kotlinx.android.synthetic.main.account_vote_producer_list_item.view.*

class VoteProducerAdapter(
    context: Context,
    interaction: PublishSubject<Interaction<String>>
) : SimpleAdapter<String>(context, interaction) {

    override fun createViewHolder(parent: ViewGroup): SimpleAdapterViewHolder<String> {
        return VoteProducerViewHolder(
            inflater.inflate(R.layout.account_vote_producer_list_item, parent, false))
    }
}

class VoteProducerViewHolder(itemView: View) : SimpleAdapterViewHolder<String>(itemView) {

    override fun populate(position: Int, value: String) {
        itemView.vote_producer_list_item_account_name.text = value
    }
}