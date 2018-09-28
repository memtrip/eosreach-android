package com.memtrip.eosreach.app.account.resources.manage.manageram

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.api.transfer.ActionReceipt
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.resources.manage.manageram.RamConfirmActivity.Companion.ramConfirmIntent
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
    private lateinit var ramPricePerKb: Balance

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.manage_ram_form_fragment, container, false)
        eosAccount = eosAccountExtra(arguments!!)
        contractAccountBalance = contractAccountBalanceExtra(arguments!!)
        ramPricePerKb = ramPricePerKb(arguments!!)
        view.manage_ram_form_amount_input.filters = arrayOf(
            CurrencyFormatInputFilter(),
            InputFilter.LengthFilter(resources.getInteger(R.integer.app_kb_input_max_length_length))
        )
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
            hideKeyboard()
            RamFormIntent.Commit(
                manage_ram_form_amount_input.editableText.toString(),
                ramCommitType
            )
        },
        RxTextView.afterTextChangeEvents(manage_ram_form_amount_input).map { event ->
            val value = if (event.editable().isNullOrEmpty()) { "0" } else {
                event.editable().toString()
            }
            RamFormIntent.ConvertKiloBytesToEOSCost(value, ramPricePerKb)
        }
    )

    override fun layout(): RamFormViewLayout = this

    override fun model(): RamFormViewModel = getViewModel(viewModelFactory)

    override fun render(): RamFormViewRenderer = render

    override fun updateEosCost(eosCost: String) {
        manage_ram_amount_form_label.text = getString(
            R.string.resources_manage_ram_form_amount_label, eosCost)
    }

    override fun navigateToConfirmRamForm(
        kilobytes: String,
        ramCommitType: RamCommitType
    ) {
        model().publish(RamFormIntent.Idle)

        startActivity(ramConfirmIntent(
            kilobytes,
            ramPricePerKb,
            ramCommitType,
            contractAccountBalance,
            context!!))
    }

    override fun emptyRamError() {
        AlertDialog.Builder(context!!)
            .setTitle(R.string.app_dialog_error_title)
            .setMessage(R.string.resources_manage_ram_form_empty)
            .setPositiveButton(R.string.app_dialog_positive_button, null)
            .create()
            .show()
    }

    companion object {

        private const val EOS_ACCOUNT_EXTRA = "EOS_ACCOUNT_EXTRA"
        private const val CONTRACT_ACCOUNT_BALANCE = "CONTRACT_ACCOUNT_BALANCE"
        private const val RAM_PRICE_PER_KB = "RAM_PRICE_PER_KB"

        fun toBundle(
            eosAccount: EosAccount,
            contractAccountBalance: ContractAccountBalance,
            ramPricePerKb: Balance
        ): Bundle = with (Bundle()) {
            putParcelable(EOS_ACCOUNT_EXTRA, eosAccount)
            putParcelable(CONTRACT_ACCOUNT_BALANCE, contractAccountBalance)
            putParcelable(RAM_PRICE_PER_KB, ramPricePerKb)
            this
        }

        private fun eosAccountExtra(bundle: Bundle): EosAccount =
            bundle.getParcelable(EOS_ACCOUNT_EXTRA)

        private fun contractAccountBalanceExtra(bundle: Bundle): ContractAccountBalance =
            bundle.getParcelable(CONTRACT_ACCOUNT_BALANCE)

        private fun ramPricePerKb(bundle: Bundle): Balance =
            bundle.getParcelable(RAM_PRICE_PER_KB)
    }
}
