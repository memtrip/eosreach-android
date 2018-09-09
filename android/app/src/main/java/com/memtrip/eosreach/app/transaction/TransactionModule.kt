package com.memtrip.eosreach.app.transaction

import com.memtrip.eosreach.app.transaction.log.TransactionLogActivity
import com.memtrip.eosreach.app.transaction.log.TransactionLogActivityModule
import com.memtrip.eosreach.app.transaction.receipt.TransactionReceiptActivity
import com.memtrip.eosreach.app.transaction.receipt.TransactionReceiptActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class TransactionModule {

    @ContributesAndroidInjector(modules = [TransactionLogActivityModule::class])
    internal abstract fun contributeTransactionLogActivity(): TransactionLogActivity

    @ContributesAndroidInjector(modules = [TransactionReceiptActivityModule::class])
    internal abstract fun contributeTransactionReceiptActivity(): TransactionReceiptActivity
}