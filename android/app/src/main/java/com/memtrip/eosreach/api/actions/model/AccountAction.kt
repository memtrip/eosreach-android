package com.memtrip.eosreach.api.actions.model

import android.os.Parcelable
import com.memtrip.eos.http.rpc.model.history.response.HistoricAccountAction
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.price.BalanceFormatter
import com.memtrip.eosreach.app.price.CurrencyPairFormatter
import com.memtrip.eosreach.utils.fullDate
import com.memtrip.eosreach.utils.toLocalDateTime
import kotlinx.android.parcel.Parcelize

sealed class AccountAction(
    val next: Int
) {

    @Parcelize
    data class Transfer(
        val actionActionSequence: Int,
        val from: String,
        val to: String,
        val memo: String,
        val formattedDate: String,
        val quantityString: String,
        val quantity: Balance,
        val currencyPairValue: String,
        val transferIncoming: Boolean,
        val transferIncomingIcon: Int,
        val transferInteractingAccountName: String,
        val contractAccountBalance: ContractAccountBalance
    ) : AccountAction(actionActionSequence), Parcelable

    companion object {

        fun createTransfer(
            action: HistoricAccountAction,
            contractAccountBalance: ContractAccountBalance
        ): AccountAction.Transfer {
            val from = action.action_trace.act.data["from"].toString()
            val to = action.action_trace.act.data["to"].toString()
            val memo = action.action_trace.act.data["memo"].toString()
            val formattedDate = action.block_time.toLocalDateTime().fullDate()
            val quantity = action.action_trace.act.data["quantity"].toString()
            val quantityBalance: Balance = BalanceFormatter.deserialize(quantity)
            val transferIncoming = (contractAccountBalance.accountName == to)
            val transferIncomingIcon = if (transferIncoming)
                R.drawable.account_actions_list_item_in_ic else R.drawable.account_actions_list_item_out_ic
            val transferInteractingAccountName: String = if (transferIncoming) from else to

            return AccountAction.Transfer(
                action.account_action_seq,
                from,
                to,
                memo,
                formattedDate,
                quantity,
                quantityBalance,
                if (!contractAccountBalance.exchangeRate.unavailable) {
                    CurrencyPairFormatter.formatAmountCurrencyPairValue(
                        quantityBalance.amount,
                        contractAccountBalance.exchangeRate)
                } else { "-" },
                transferIncoming,
                transferIncomingIcon,
                transferInteractingAccountName,
                contractAccountBalance)
        }
    }
}