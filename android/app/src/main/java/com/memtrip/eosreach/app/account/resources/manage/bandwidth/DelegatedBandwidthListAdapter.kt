package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.bandwidth.DelegatedBandwidth
import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.SimpleAdapter
import com.memtrip.eosreach.uikit.SimpleAdapterViewHolder
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.bandwidth_delegate_list_item.view.*

class DelegateBandwidthListAdapter(
    context: Context,
    interaction: PublishSubject<Interaction<DelegatedBandwidth>>
) : SimpleAdapter<DelegatedBandwidth>(context, interaction) {

    override fun createViewHolder(parent: ViewGroup): SimpleAdapterViewHolder<DelegatedBandwidth> {
        return DelegateBandwidthListViewHolder(
            inflater.inflate(R.layout.bandwidth_delegate_list_item, parent, false))
    }

    override fun defaultRowMargin(position: Int, view: View) {
        if (position == data.size - 1) {
            (view.layoutParams as RecyclerView.LayoutParams).bottomMargin = marginSize
        } else {
            (view.layoutParams as RecyclerView.LayoutParams).bottomMargin = 0
        }
    }
}

class DelegateBandwidthListViewHolder(itemView: View) : SimpleAdapterViewHolder<DelegatedBandwidth>(itemView) {

    override fun populate(position: Int, value: DelegatedBandwidth) {
        itemView.bandwidth_delegate_list_item_account_name.text = value.accountName
        itemView.bandwidth_delegate_list_cpu_value.text = value.cpuWeight
        itemView.bandwidth_delegate_list_net_value.text = value.netWeight
    }
}