package com.memtrip.eosreach.app.account.actions

import android.content.Context
import android.view.View
import android.view.ViewGroup

import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.actions.model.AccountAction

import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.SimpleAdapter
import com.memtrip.eosreach.uikit.SimpleAdapterViewHolder

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
        when (value) {
            is AccountAction.Transfer -> {
                itemView.account_actions_list_item_interaction_account_name.text = value.transferInteractingAccountName
                itemView.account_actions_list_item_icon.setImageDrawable(itemView.context.getDrawable(value.transferIncomingIcon))
                itemView.account_actions_list_item_interaction_crypto_value.text = value.quantityString
                itemView.account_actions_list_item_interaction_fiat_value.text = value.memo
                itemView.account_actions_list_item_date_created.text = value.formattedDate
            }
        }
    }
}