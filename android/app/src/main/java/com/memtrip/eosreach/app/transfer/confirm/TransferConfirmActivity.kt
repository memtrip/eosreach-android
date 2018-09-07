package com.memtrip.eosreach.app.transfer.confirm

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle

import com.jakewharton.rxbinding2.view.RxView

import com.memtrip.eosreach.R

import com.memtrip.eosreach.api.transfer.TransferReceipt
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory

import com.memtrip.eosreach.app.transfer.form.TransferFormData
import com.memtrip.eosreach.app.transaction.receipt.TransactionReceiptActivity.Companion.transactionReceiptIntent
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible
import com.memtrip.eosreach.app.price.BalanceParser
import com.memtrip.eosreach.app.transaction.log.TransactionLogActivity.Companion.transactionLogIntent
import com.memtrip.eosreach.uikit.invisible
import dagger.android.AndroidInjection

import io.reactivex.Observable
import kotlinx.android.synthetic.main.transfer_confirm_activity.*
import javax.inject.Inject

class TransferConfirmActivity
    : MviActivity<TransferConfirmIntent, TransferConfirmRenderAction, TransferConfirmViewState, TransferConfirmViewLayout>(), TransferConfirmViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: TransferConfirmViewRenderer

    lateinit var transferFormData: TransferFormData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transfer_confirm_activity)
        setSupportActionBar(transfer_confirm_toolbar)
        supportActionBar!!.title = getString(R.string.transfer_confirm_toolbar_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        transferFormData = transferFormDataExtra(intent)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<TransferConfirmIntent> = Observable.merge(
        Observable.just(TransferConfirmIntent.Init(transferFormData)),
        RxView.clicks(transfer_confirm_confirm_button).map {
            TransferConfirmIntent.Transfer(
                TransferRequestData(
                    transferFormData.contractAccountBalance.accountName,
                    transferFormData.username,
                    BalanceParser.create(
                        transferFormData.amount,
                        transferFormData.contractAccountBalance.balance.symbol
                    ),
                    transferFormData.memo
                )
            )
        }
    )

    override fun layout(): TransferConfirmViewLayout = this

    override fun model(): TransferConfirmViewModel = getViewModel(viewModelFactory)

    override fun render(): TransferConfirmViewRenderer = render

    override fun populate(transferFormData: TransferFormData) {
        transfer_confirm_details_amount_value.text = getString(
            R.string.transfer_confirm_crypto_amount,
            transferFormData.amount,
            BalanceParser.format(transferFormData.contractAccountBalance.balance)
        )

        transfer_confirm_details_to_value.text = transferFormData.username
        transfer_confirm_details_from_value.text = transferFormData.contractAccountBalance.accountName
        transfer_confirm_details_memo_value.text = transferFormData.memo
    }

    override fun showProgress() {
        transfer_confirm_progress.visible()
        transfer_confirm_confirm_button.invisible()
    }

    override fun onSuccess(transferReceipt: TransferReceipt) {
        startActivity(transactionReceiptIntent(TransferReceipt(transferReceipt.transactionId), this))
        finish()
    }

    override fun showError(message: String, log: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(R.string.transaction_view_log_position_button) { _, _ ->
                model().publish(TransferConfirmIntent.ViewLog(log))
            }
            .setNegativeButton(R.string.transaction_view_log_negative_button, null)
            .create()
            .show()

        transfer_confirm_progress.gone()
        transfer_confirm_confirm_button.visible()
    }

    override fun viewLog(log: String) {
        model().publish(TransferConfirmIntent.Idle)
        startActivity(transactionLogIntent(log, this))
    }

    companion object {

        private const val TRANSFER_FORM_EXTRA = "TRANSFER_FORM_EXTRA"

        fun transferConfirmIntent(transferFormData: TransferFormData, context: Context): Intent {
            return with (Intent(context, TransferConfirmActivity::class.java)) {
                putExtra(TRANSFER_FORM_EXTRA, transferFormData)
                this
            }
        }

        fun transferFormDataExtra(intent: Intent): TransferFormData {
            return intent.getParcelableExtra(TRANSFER_FORM_EXTRA) as TransferFormData
        }
    }
}
