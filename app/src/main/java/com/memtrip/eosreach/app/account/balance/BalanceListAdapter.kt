package com.memtrip.eosreach.app.account.balance

import android.content.Context
import android.view.View
import android.view.ViewGroup

import com.memtrip.eosreach.R

import com.memtrip.eosreach.api.balance.Balance

import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.SimpleAdapter
import com.memtrip.eosreach.uikit.SimpleAdapterViewHolder
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.account_balance_list_item.view.*

class BalanceListAdapter(
    context: Context,
    interaction: PublishSubject<Interaction<Balance>>
) : SimpleAdapter<Balance>(context, interaction) {

    override fun createViewHolder(parent: ViewGroup): SimpleAdapterViewHolder<Balance> {
        return BalanceViewHolder(
            inflater.inflate(R.layout.account_balance_list_item, parent, false))
    }
}

class BalanceViewHolder(itemView: View) : SimpleAdapterViewHolder<Balance>(itemView) {

    override fun populate(position: Int, value: Balance) {
        itemView.welcome_accounts_list_item_account_name.text = value.symbol
        itemView.welcome_accounts_list_item_balance.text = "${value.amount}"
    }
}