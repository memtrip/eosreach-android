package com.memtrip.eosreach.wallet

interface Wallet {
    fun create(walletName: String)
    fun importKey(walletName: String, privateKey: ByteArray)
    fun getKey(walletName: String): ByteArray

    class NotFoundException : RuntimeException()
}
