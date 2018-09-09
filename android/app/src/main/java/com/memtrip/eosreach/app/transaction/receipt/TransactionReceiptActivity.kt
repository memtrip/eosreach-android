package com.memtrip.eosreach.app.transaction.receipt

import android.content.Context
import android.content.Intent

import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eos.http.rpc.model.transaction.response.TransactionReceipt
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.transfer.TransferReceipt
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.AccountActivity.Companion.accountIntent
import com.memtrip.eosreach.app.account.AccountBundle
import com.memtrip.eosreach.app.account.actions.ActionsActivity.Companion.actionsIntent

import dagger.android.AndroidInjection

import io.reactivex.Observable
import kotlinx.android.synthetic.main.transaction_receipt_activity.*
import javax.inject.Inject

class TransactionReceiptActivity
    : MviActivity<TransactionReceiptIntent, TransferReceiptRenderAction, TransactionReceiptViewState, TransferReceiptViewLayout>(), TransferReceiptViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: TransferReceiptViewRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transaction_receipt_activity)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<TransactionReceiptIntent> {
        return RxView.clicks(transaction_receipt_done_button).map {
            TransactionReceiptIntent.NavigateToActions
        }
    }

    override fun layout(): TransferReceiptViewLayout = this

    override fun model(): TransactionReceiptViewModel = getViewModel(viewModelFactory)

    override fun render(): TransferReceiptViewRenderer = render

    override fun populate(transactionReceipt: TransactionReceipt) {
    }

    override fun navigateToActions() {
        val contractAccountBalance = transferReceiptExtra(intent).contractAccountBalance
        startActivities(
            arrayOf(
                with(accountIntent(AccountBundle(
                    contractAccountBalance.accountName,
                    contractAccountBalance.balance.amount,
                    contractAccountBalance.balance.symbol
                ), this)) {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK)
                    this
                },
                actionsIntent(
                    transferReceiptExtra(intent).contractAccountBalance,
                    this)
            )
        )
    }

    override fun onBackPressed() {
        navigateToActions()
    }

    companion object {

        private const val TRANSFER_RECEIPT = "TRANSFER_RECEIPT"

        fun transactionReceiptIntent(transferReceipt: TransferReceipt, context: Context): Intent {
            return with (Intent(context, TransactionReceiptActivity::class.java)) {
                putExtra(TRANSFER_RECEIPT, transferReceipt)
                this
            }
        }

        fun transferReceiptExtra(intent: Intent): TransferReceipt {
            return intent.getParcelableExtra(TRANSFER_RECEIPT) as TransferReceipt
        }
    }
}
