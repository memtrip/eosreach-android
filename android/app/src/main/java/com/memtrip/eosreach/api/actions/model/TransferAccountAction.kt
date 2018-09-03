package com.memtrip.eosreach.api.actions

import org.json.JSONObject
import org.threeten.bp.LocalDateTime

data class TransferAccountAction(
    val from: String,
    val to: String,
    val quantity: String,
    val memo: String,
    override val transactionId: String,
    override val receiverAccountName: String,
    override val actAccountName: String,
    override val actionType: String,
    override val actionData: JSONObject,
    override val date: LocalDateTime
) : AccountAction()