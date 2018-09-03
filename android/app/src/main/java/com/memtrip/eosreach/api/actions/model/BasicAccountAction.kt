package com.memtrip.eosreach.api.actions

import org.threeten.bp.LocalDateTime

class BasicAccountAction(
    override val transactionId: String,
    override val receiverAccountName: String,
    override val actAccountName: String,
    override val actionType: String,
    override val actionData: Any,
    override val date: LocalDateTime
) : AccountAction()