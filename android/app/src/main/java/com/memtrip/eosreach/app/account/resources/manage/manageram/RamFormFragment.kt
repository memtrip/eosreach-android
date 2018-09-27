package com.memtrip.eosreach.app.account.resources.manage.manageram

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.balance.ContractAccountBalance
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
import kotlinx.android.synthetic.main.manage_ram_form_fragment.*
import kotlinx.android.synthetic.main.manage_ram_form_fragment.view.*
import javax.inject.Inject

abstract class RamFormFragment
    : MviFragment<RamFormIntent, RamFormRenderAction, RamFormViewState, RamFormViewLayout>(), RamFormViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: RamFormViewRenderer

    abstract fun buttonLabel(): String

    abstract val ramCommitType: RamCommitType

    private lateinit var eosAccount: EosAccount
    private lateinit var contractAccountBalance: ContractAccountBalance

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.manage_ram_form_fragment, container, false)
        eosAccount = eosAccountExtra(arguments!!)
        contractAccountBalance = contractAccountBalanceExtra(arguments!!)
        view.manage_ram_form_amount_input.filters = arrayOf(CurrencyFormatInputFilter())
        view.manage_ram_form_cta_button.text = buttonLabel()
        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<RamFormIntent> = Observable.merge(
        Observable.just(RamFormIntent.Init),
        Observable.merge(
            RxView.clicks(manage_ram_form_cta_button),
            RxTextView.editorActions(manage_ram_form_amount_input)
        ).map {
            RamFormIntent.Commit(
                manage_ram_form_amount_input.editableText.toString(),
                ramCommitType
            )
        }
    )

    override fun layout(): RamFormViewLayout = this

    override fun model(): RamFormViewModel = getViewModel(viewModelFactory)

    override fun render(): RamFormViewRenderer = render

    override fun showProgress() {
        manage_ram_form_progress.visible()
        manage_ram_form_cta_button.invisible()
    }

    override fun showError(message: String, log: String) {
        manage_ram_form_progress.gone()
        manage_ram_form_cta_button.visible()

        AlertDialog.Builder(context!!)
            .setMessage(message)
            .setPositiveButton(R.string.transaction_view_log_position_button) { _, _ ->
                model().publish(RamFormIntent.Idle)
                startActivity(transactionLogIntent(log, context!!))
            }
            .setNegativeButton(R.string.transaction_view_log_negative_button, null)
            .create()
            .show()
    }

    override fun showSuccess(transactionId: String) {
        startActivity(transactionReceiptIntent(
            ActionReceipt(transactionId, eosAccount.accountName),
            contractAccountBalance,
            TransactionReceiptRoute.ACCOUNT,
            context!!))
        activity!!.finish()
    }

    companion object {

        private const val EOS_ACCOUNT_EXTRA = "EOS_ACCOUNT_EXTRA"
        private const val CONTRACT_ACCOUNT_BALANCE = "CONTRACT_ACCOUNT_BALANCE"

        fun toBundle(eosAccount: EosAccount): Bundle = with (Bundle()) {
            putParcelable(EOS_ACCOUNT_EXTRA, eosAccount)
            this
        }

        private fun eosAccountExtra(bundle: Bundle): EosAccount =
            bundle.getParcelable(EOS_ACCOUNT_EXTRA)

        private fun contractAccountBalanceExtra(bundle: Bundle): ContractAccountBalance =
            bundle.getParcelable(CONTRACT_ACCOUNT_BALANCE)
    }
}
