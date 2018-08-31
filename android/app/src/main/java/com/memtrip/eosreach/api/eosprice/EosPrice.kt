package com.memtrip.eosreach.api.eosprice

data class EosPrice(
    val value: Double,
    val currency: String,
    val outOfDate: Boolean = false,
    val unavailable: Boolean = false
) {

    companion object {
        fun unavailable(): EosPrice {
            return EosPrice(
                (-1).toDouble(),
                "-",
                false,
                true
            )
        }
    }
}