package com.memtrip.eosreach.utils

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

fun LocalDateTime.fullDate(): String = DateTimeFormatter.ofPattern(
    "EEEE MMMM dd 'at' HH:mm"
).format(this)