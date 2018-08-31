package com.memtrip.eosreach.app.accountlist

import android.content.Context
import android.view.View
import android.view.ViewGroup

import com.memtrip.eosreach.R

import com.memtrip.eosreach.db.AccountEntity

import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.SimpleAdapter
import com.memtrip.eosreach.uikit.SimpleAdapterViewHolder
import io.reactivex.subjects.PublishSubject

import kotlinx.android.synthetic.main.accounts_list_item.view.*

class AccountListAdapter(
    context: Context,
    interaction: PublishSubject<Interaction<AccountEntity>>
) : SimpleAdapter<AccountEntity>(context, interaction) {

    override fun createViewHolder(parent: ViewGroup): SimpleAdapterViewHolder<AccountEntity> {
        return AccountViewHolder(
            inflater.inflate(R.layout.accounts_list_item, parent, false))
    }
}

class AccountViewHolder(itemView: View) : SimpleAdapterViewHolder<AccountEntity>(itemView) {

    override fun populate(position: Int, value: AccountEntity) {
        itemView.accounts_list_item_account_name.text = value.accountName

        if (value.symbol != null && value.balance != null) {
            itemView.accounts_list_item_balance.text = itemView.context.getString(
                R.string.accounts_list_balance,
                value.balance.toString(),
                value.symbol)
        } else {
            itemView.accounts_list_item_balance.text = itemView.context.getString(
                R.string.accounts_list_balance_empty)
        }
    }
}