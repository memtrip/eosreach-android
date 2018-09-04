package com.memtrip.eosreach.app.transfer.receipt

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.transfer.TransferReceipt
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import dagger.android.AndroidInjection

import io.reactivex.Observable
import javax.inject.Inject

class TransferReceiptActivity
    : MviActivity<TransferReceiptIntent, TransferReceiptRenderAction, TransferReceiptViewState, TransferReceiptViewLayout>(), TransferReceiptViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: TransferReceiptViewRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transfer_receipt_activity)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<TransferReceiptIntent> = Observable.empty()

    override fun layout(): TransferReceiptViewLayout = this

    override fun model(): TransferReceiptViewModel = getViewModel(viewModelFactory)

    override fun render(): TransferReceiptViewRenderer = render

    override fun showProgress() {

    }

    override fun showError() {

    }

    companion object {

        private const val TRANSFER_RECEIPT = "TRANSFER_RECEIPT"

        fun transferReceiptIntent(transferReceipt: TransferReceipt, context: Context): Intent {
            return with (Intent(context, TransferReceiptActivity::class.java)) {
                putExtra(TRANSFER_RECEIPT, transferReceipt)
                addFlags(FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK)
                this
            }
        }

        fun transferReceiptExtra(intent: Intent): TransferReceipt {
            return intent.getParcelableExtra(TRANSFER_RECEIPT) as TransferReceipt
        }
    }
}
