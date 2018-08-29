package com.memtrip.eosreach.app.welcome.accountlist

import android.content.Context
import android.view.View
import android.view.ViewGroup

import com.memtrip.eosreach.R
import com.memtrip.eosreach.db.AccountEntity
import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.SimpleAdapter
import com.memtrip.eosreach.uikit.SimpleAdapterViewHolder
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.welcome_account_list_item.view.*

class AccountListAdapter(
    context: Context,
    interaction: PublishSubject<Interaction<AccountEntity>>
) : SimpleAdapter<AccountEntity>(context, interaction) {

    override fun createViewHolder(parent: ViewGroup): SimpleAdapterViewHolder<AccountEntity> {
        return AccountListViewHolder(
            inflater.inflate(R.layout.welcome_account_list_item, parent, false))
    }
}

class AccountListViewHolder(itemView: View) : SimpleAdapterViewHolder<AccountEntity>(itemView) {

    override fun populate(position: Int, value: AccountEntity) {
        itemView.welcome_accounts_list_item_account_name.text = value.accountName
        itemView.welcome_accounts_list_item_balance.text = "100.23 EOS"
    }
}