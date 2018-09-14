package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.transfer.ActionReceipt
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.transaction.log.TransactionLogActivity
import com.memtrip.eosreach.app.transaction.receipt.TransactionReceiptActivity
import com.memtrip.eosreach.app.transaction.receipt.TransactionReceiptRoute
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.invisible
import com.memtrip.eosreach.uikit.visible

import dagger.android.AndroidInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.bandwidth_confirm_activity.*
import javax.inject.Inject

import kotlinx.android.synthetic.main.manage_bandwidth_form_fragment.*
import kotlinx.android.synthetic.main.settings_activity.*

class BandwidthConfirmActivity
    : MviActivity<BandwidthConfirmIntent, BandwidthConfirmRenderAction, BandwidthConfirmViewState, BandwidthConfirmViewLayout>(), BandwidthConfirmViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: BandwidthConfirmViewRenderer

    lateinit var bandwidthBundle: BandwidthBundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bandwidth_confirm_activity)
        setSupportActionBar(bandwidth_confirm_toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        when (bandwidthBundle.bandwidthCommitType) {
            BandwidthCommitType.DELEGATE -> {
                supportActionBar!!.title = getString(R.string.bandwidth_confirm_delegate_title)
            }
            BandwidthCommitType.UNDELEGATE -> {
                supportActionBar!!.title = getString(R.string.bandwidth_confirm_undelegate_title)
            }
        }
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<BandwidthConfirmIntent> = Observable.empty()

    override fun layout(): BandwidthConfirmViewLayout = this

    override fun model(): BandwidthConfirmViewModel = getViewModel(viewModelFactory)

    override fun render(): BandwidthConfirmViewRenderer = render

    override fun populate(bandwidthBundle: BandwidthBundle) {
        when (bandwidthBundle.bandwidthCommitType) {
            BandwidthCommitType.DELEGATE -> {
                bandwidth_confirm_cta_button.text = getString(R.string.bandwidth_confirm_delegate_button)
            }
            BandwidthCommitType.UNDELEGATE -> {
                bandwidth_confirm_cta_button.text = getString(R.string.bandwidth_confirm_undelegate_button)
            }
        }
    }

    override fun showProgress() {
        manage_bandwidth_form_progress.visible()
        manage_bandwidth_form_cta_button.invisible()
    }

    override fun showError(message: String, log: String) {
        manage_bandwidth_form_progress.gone()
        manage_bandwidth_form_cta_button.visible()

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
        startActivity(TransactionReceiptActivity.transactionReceiptIntent(
            ActionReceipt(transactionId, bandwidthBundle.fromAccount),
            TransactionReceiptRoute.ACCOUNT,
            this))
        finish()
    }

    companion object {

        private const val BANDWIDTH_BUNDLE_EXTRA = "BANDWIDTH_BUNDLE_EXTRA"

        fun confirmBandwidthIntent(bandwidthBundle: BandwidthBundle, context: Context): Intent {
            return with (Intent(context, BandwidthConfirmActivity::class.java)) {
                putExtra(BANDWIDTH_BUNDLE_EXTRA, bandwidthBundle)
                this
            }
        }

        fun bandwidthBundleExtra(intent: Intent): BandwidthBundle {
            return intent.getParcelableExtra(BANDWIDTH_BUNDLE_EXTRA) as BandwidthBundle
        }
    }
}
