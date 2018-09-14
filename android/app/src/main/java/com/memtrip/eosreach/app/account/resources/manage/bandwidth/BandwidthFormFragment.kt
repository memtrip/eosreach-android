package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.transfer.ActionReceipt
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.transaction.log.TransactionLogActivity.Companion.transactionLogIntent
import com.memtrip.eosreach.app.transaction.receipt.TransactionReceiptActivity.Companion.transactionReceiptIntent
import com.memtrip.eosreach.app.transaction.receipt.TransactionReceiptRoute
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.inputfilter.CurrencyFormatInputFilter
import com.memtrip.eosreach.uikit.invisible
import com.memtrip.eosreach.uikit.visible

import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.manage_bandwidth_form_fragment.*
import kotlinx.android.synthetic.main.manage_bandwidth_form_fragment.view.*
import javax.inject.Inject

abstract class BandwidthFormFragment
    : MviFragment<BandwidthFormIntent, BandwidthFormRenderAction, BandwidthFormViewState, BandwidthFormViewLayout>(), BandwidthFormViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: BandwidthFormViewRenderer

    private lateinit var eosAccount: EosAccount

    abstract fun buttonLabel(): String

    abstract val bandwidthCommitType: BandwidthCommitType

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.manage_bandwidth_form_fragment, container, false)
        eosAccount = eosAccountExtra(arguments!!)
        view.manage_bandwidth_net_amount_form_input.filters = arrayOf(CurrencyFormatInputFilter())
        view.manage_bandwidth_cpu_amount_form_input.filters = arrayOf(CurrencyFormatInputFilter())
        view.manage_bandwidth_form_cta_button.text = buttonLabel()
        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<BandwidthFormIntent> = Observable.merge(
        Observable.just(BandwidthFormIntent.Init),
        Observable.merge(
            RxView.clicks(manage_bandwidth_form_cta_button),
            RxTextView.editorActions(manage_bandwidth_cpu_amount_form_input)
        ).map {
            BandwidthFormIntent.Commit(
                manage_bandwidth_net_amount_form_input.editableText.toString(),
                manage_bandwidth_cpu_amount_form_input.editableText.toString(),
                bandwidthCommitType
            )
        }
    )

    override fun layout(): BandwidthFormViewLayout = this

    override fun model(): BandwidthFormViewModel = getViewModel(viewModelFactory)

    override fun render(): BandwidthFormViewRenderer = render

    override fun showProgress() {
        manage_bandwidth_form_progress.visible()
        manage_bandwidth_form_cta_button.invisible()
    }

    override fun showError(message: String, log: String) {
        manage_bandwidth_form_progress.gone()
        manage_bandwidth_form_cta_button.visible()

        AlertDialog.Builder(context!!)
            .setMessage(message)
            .setPositiveButton(R.string.transaction_view_log_position_button) { _, _ ->
                model().publish(BandwidthFormIntent.Idle)
                startActivity(transactionLogIntent(log, context!!))
            }
            .setNegativeButton(R.string.transaction_view_log_negative_button, null)
            .create()
            .show()
    }

    override fun showSuccess(transactionId: String) {
        startActivity(transactionReceiptIntent(
            ActionReceipt(transactionId, eosAccount.accountName),
            TransactionReceiptRoute.ACCOUNT,
            context!!))
        activity!!.finish()
    }

    companion object {

        private const val EOS_ACCOUNT_EXTRA = "EOS_ACCOUNT_EXTRA"

        fun toBundle(eosAccount: EosAccount): Bundle = with (Bundle()) {
            putParcelable(EOS_ACCOUNT_EXTRA, eosAccount)
            this
        }

        private fun eosAccountExtra(bundle: Bundle): EosAccount =
            bundle.getParcelable(EOS_ACCOUNT_EXTRA)
    }
}
