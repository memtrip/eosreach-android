package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.api.transfer.ActionReceipt
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.transaction.log.TransactionLogActivity
import com.memtrip.eosreach.app.transaction.receipt.TransactionReceiptActivity.Companion.transactionReceiptIntent
import com.memtrip.eosreach.app.transaction.receipt.TransactionReceiptRoute
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.invisible
import com.memtrip.eosreach.uikit.visible
import dagger.android.AndroidInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.bandwidth_confirm_activity.*
import javax.inject.Inject

class BandwidthConfirmActivity
    : MviActivity<BandwidthConfirmIntent, BandwidthConfirmRenderAction, BandwidthConfirmViewState, BandwidthConfirmViewLayout>(), BandwidthConfirmViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: BandwidthConfirmViewRenderer

    lateinit var bandwidthBundle: BandwidthBundle
    lateinit var contractAccountBalance: ContractAccountBalance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bandwidth_confirm_activity)
        setSupportActionBar(bandwidth_confirm_toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        bandwidthBundle = bandwidthBundleExtra(intent)
        contractAccountBalance = contractAccountBalanceExtra(intent)

        when (bandwidthBundle.bandwidthCommitType) {
            BandwidthCommitType.DELEGATE -> {
                supportActionBar!!.title = getString(R.string.resources_bandwidth_confirm_delegate_title)
            }
            BandwidthCommitType.UNDELEGATE -> {
                supportActionBar!!.title = getString(R.string.resources_bandwidth_confirm_undelegate_title)
            }
        }
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<BandwidthConfirmIntent> = Observable.merge(
        Observable.just(BandwidthConfirmIntent.Init(bandwidthBundle)),
        RxView.clicks(bandwidth_confirm_cta_button).map {
            BandwidthConfirmIntent.Commit(bandwidthBundle)
        }
    )

    override fun layout(): BandwidthConfirmViewLayout = this

    override fun model(): BandwidthConfirmViewModel = getViewModel(viewModelFactory)

    override fun render(): BandwidthConfirmViewRenderer = render

    override fun populate(bandwidthBundle: BandwidthBundle) {
        bandwidth_confirm_details_layout.populate(
            bandwidthBundle.cpuAmount,
            bandwidthBundle.netAmount,
            contractAccountBalance)
    }

    override fun showProgress() {
        bandwidth_confirm_progress.visible()
        bandwidth_confirm_cta_button.invisible()
    }

    override fun showError(message: String, log: String) {
        bandwidth_confirm_progress.gone()
        bandwidth_confirm_cta_button.visible()

        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(R.string.transaction_view_log_position_button) { _, _ ->
                model().publish(BandwidthConfirmIntent.Idle)
                startActivity(TransactionLogActivity.transactionLogIntent(log, this))
            }
            .setNegativeButton(R.string.transaction_view_log_negative_button, null)
            .create()
            .show()
    }

    override fun showSuccess(transactionId: String) {
        startActivity(transactionReceiptIntent(
            ActionReceipt(transactionId, bandwidthBundle.fromAccount),
            contractAccountBalance,
            TransactionReceiptRoute.ACCOUNT,
            this))
        finish()
    }

    companion object {

        private const val BANDWIDTH_BUNDLE = "BANDWIDTH_BUNDLE"
        private const val CONTRACT_ACCOUNT_BALANCE = "CONTRACT_ACCOUNT_BALANCE"

        fun confirmBandwidthIntent(
            bandwidthBundle: BandwidthBundle,
            contractAccountBalance: ContractAccountBalance,
            context: Context
        ): Intent {
            return with (Intent(context, BandwidthConfirmActivity::class.java)) {
                putExtra(BANDWIDTH_BUNDLE, bandwidthBundle)
                putExtra(CONTRACT_ACCOUNT_BALANCE, contractAccountBalance)
                this
            }
        }

        fun bandwidthBundleExtra(intent: Intent): BandwidthBundle =
            intent.getParcelableExtra(BANDWIDTH_BUNDLE)

        private fun contractAccountBalanceExtra(intent: Intent): ContractAccountBalance =
            intent.getParcelableExtra(CONTRACT_ACCOUNT_BALANCE)
    }
}
