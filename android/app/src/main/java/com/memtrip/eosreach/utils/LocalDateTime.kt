package com.memtrip.eosreach.utils

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

fun LocalDateTime.fullDate(): String = DateTimeFormatter.ofPattern(
    "EEE dd MMM 'at' HH:mm"
).format(this)