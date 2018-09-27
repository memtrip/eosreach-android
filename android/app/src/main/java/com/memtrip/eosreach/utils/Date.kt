package com.memtrip.eosreach.utils

import java.util.Calendar
import java.util.Date

fun transactionDefaultExpiry(): Date = with (Calendar.getInstance()) {
    set(Calendar.MINUTE, get(Calendar.MINUTE) + 2)
    this
}.time