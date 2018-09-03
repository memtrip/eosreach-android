package com.memtrip.eosreach.api.actions

import org.threeten.bp.LocalDateTime

abstract class AccountAction {
    abstract val transactionId: String
    abstract val receiverAccountName: String
    abstract val actAccountName: String
    abstract val actionType: String
    abstract val actionData: Any
    abstract val date: LocalDateTime
}