package com.memtrip.eosreach.app.account.actions

import android.content.Context
import android.view.View
import android.view.ViewGroup

import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.actions.model.AccountAction
import com.memtrip.eosreach.api.actions.model.TransferAccountAction

import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.SimpleAdapter
import com.memtrip.eosreach.uikit.SimpleAdapterViewHolder
import com.memtrip.eosreach.utils.fullDate
import com.memtrip.eosreach.utils.toLocalDateTime

import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.account_actions_list_item.view.*

class AccountActionsAdapter(
    context: Context,
    interaction: PublishSubject<Interaction<AccountAction>>
) : SimpleAdapter<AccountAction>(context, interaction) {

    override fun createViewHolder(parent: ViewGroup): SimpleAdapterViewHolder<AccountAction> {
        return AccountActionsViewHolder(
            inflater.inflate(R.layout.account_actions_list_item, parent, false))
    }
}

class AccountActionsViewHolder(itemView: View) : SimpleAdapterViewHolder<AccountAction>(itemView) {

    override fun populate(position: Int, value: AccountAction) {
        itemView.account_actions_list_item_interaction_account_name.text = value.interactionAccountName
        itemView.account_actions_list_item_date_created.text = value.date.toLocalDateTime().fullDate()

        if (value.incoming) {
            itemView.account_actions_list_item_icon.setImageDrawable(
                itemView.context.getDrawable(R.drawable.account_actions_list_item_in_ic))
        } else {
            itemView.account_actions_list_item_icon.setImageDrawable(
                itemView.context.getDrawable(R.drawable.account_actions_list_item_out_ic))
        }

        when (value) {
            is TransferAccountAction -> {

            }
        }
    }
}