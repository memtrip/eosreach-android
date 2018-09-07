package com.memtrip.eosreach.app.transfer

import com.memtrip.eosreach.app.transfer.confirm.TransferConfirmActivity
import com.memtrip.eosreach.app.transfer.confirm.TransferConfirmActivityModule
import com.memtrip.eosreach.app.transfer.form.TransferFormActivity
import com.memtrip.eosreach.app.transfer.form.TransferFormActivityModule
import com.memtrip.eosreach.app.transaction.receipt.TransactionReceiptActivity
import com.memtrip.eosreach.app.transaction.receipt.TransactionReceiptActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class TransferModule {

    @ContributesAndroidInjector(modules = [TransferFormActivityModule::class])
    internal abstract fun contributeTransferActivity(): TransferFormActivity


    @ContributesAndroidInjector(modules = [TransferConfirmActivityModule::class])
    internal abstract fun contributeTransferConfirmActivity(): TransferConfirmActivity


    @ContributesAndroidInjector(modules = [TransactionReceiptActivityModule::class])
    internal abstract fun contributeTransferReceiptActivity(): TransactionReceiptActivity
}