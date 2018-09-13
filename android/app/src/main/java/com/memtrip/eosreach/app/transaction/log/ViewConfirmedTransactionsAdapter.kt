package com.memtrip.eosreach.app.transaction.log

import android.content.Context
import android.view.View
import android.view.ViewGroup

import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.price.BalanceFormatter
import com.memtrip.eosreach.db.transaction.TransactionLogEntity

import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.SimpleAdapter
import com.memtrip.eosreach.uikit.SimpleAdapterViewHolder
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.account_balance_list_item.view.*
import kotlinx.android.synthetic.main.transaction_view_confirmed_list_item.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

class ViewConfirmedTransactionsAdapter(
    context: Context,
    interaction: PublishSubject<Interaction<TransactionLogEntity>>
) : SimpleAdapter<TransactionLogEntity>(context, interaction) {

    override fun createViewHolder(parent: ViewGroup): SimpleAdapterViewHolder<TransactionLogEntity> {
        return ViewConfirmedTransactionsViewHolder(
            inflater.inflate(R.layout.transaction_view_confirmed_list_item, parent, false))
    }
}

class ViewConfirmedTransactionsViewHolder(itemView: View) : SimpleAdapterViewHolder<TransactionLogEntity>(itemView) {

    override fun populate(position: Int, value: TransactionLogEntity) {
        itemView.transaction_view_confirmed_list_item_id.text = value.transactionId
        itemView.transaction_view_confirmed_list_item_date.text = value.formattedDate
    }
}