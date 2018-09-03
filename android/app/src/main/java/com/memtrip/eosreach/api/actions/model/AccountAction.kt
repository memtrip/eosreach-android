package com.memtrip.eosreach.api.actions.model

import com.memtrip.eos.http.rpc.model.history.response.HistoricAccountAction

import org.threeten.bp.LocalDateTime

abstract class AccountAction(
    accountName: String,
    action: HistoricAccountAction,
    val transactionId: String = action.action_trace.trx_id,
    private val receiverAccountName: String = action.action_trace.receipt.receiver,
    private val senderAccountName: String = action.action_trace.act.account,
    val outgoing: Boolean = (senderAccountName == accountName),
    val interactionAccountName: String = if (outgoing) senderAccountName else receiverAccountName,
    val actionType: String = action.action_trace.act.name,
    val actionData: Map<String, Any> = action.action_trace.act.data,
    val date: LocalDateTime = action.block_time
) {

    companion object {

        fun create(accountName: String, action: HistoricAccountAction): AccountAction = when (action.action_trace.act.name) {
            "transfer" -> TransferAccountAction(accountName, action)
            else -> BasicAccountAction(accountName, action)
        }
    }
}