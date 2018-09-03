package com.memtrip.eosreach.app.account.balance

import android.content.Context
import android.view.View
import android.view.ViewGroup

import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.AccountBalance

import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.SimpleAdapter
import com.memtrip.eosreach.uikit.SimpleAdapterViewHolder
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.account_balance_list_item.view.*

class AccountBalanceListAdapter(
    context: Context,
    interaction: PublishSubject<Interaction<AccountBalance>>
) : SimpleAdapter<AccountBalance>(context, interaction) {

    override fun createViewHolder(parent: ViewGroup): SimpleAdapterViewHolder<AccountBalance> {
        return AccountBalanceViewHolder(
            inflater.inflate(R.layout.account_balance_list_item, parent, false))
    }
}

class AccountBalanceViewHolder(itemView: View) : SimpleAdapterViewHolder<AccountBalance>(itemView) {

    override fun populate(position: Int, value: AccountBalance) {
        itemView.welcome_accounts_list_item_account_name.text = value.balance.symbol
        itemView.welcome_accounts_list_item_balance.text = "${value.balance.amount}"
    }
}