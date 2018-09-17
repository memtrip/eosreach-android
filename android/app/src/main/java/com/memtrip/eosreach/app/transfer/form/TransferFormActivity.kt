package com.memtrip.eosreach.app.transfer.form

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.transfer.confirm.TransferConfirmActivity.Companion.transferConfirmIntent
import com.memtrip.eosreach.uikit.inputfilter.AccountNameInputFilter
import com.memtrip.eosreach.uikit.inputfilter.CurrencyFormatInputFilter
import dagger.android.AndroidInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.transfer_form_activity.*
import javax.inject.Inject


class TransferFormActivity
    : MviActivity<TransferFormIntent, TransferFormRenderAction, TransferFormViewState, TransferFormViewLayout>(), TransferFormViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: TransferFormViewRenderer

    lateinit var contractAccountBalance: ContractAccountBalance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transfer_form_activity)
        setSupportActionBar(account_transfer_toolbar)
        supportActionBar!!.title = getString(R.string.transfer_form_toolbar_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        account_transfer_to_input.filters = arrayOf(
            AccountNameInputFilter(),
            InputFilter.LengthFilter(resources.getInteger(R.integer.app_account_name_length)))
        account_transfer_amount.filters = arrayOf(
            CurrencyFormatInputFilter(),
            InputFilter.LengthFilter(resources.getInteger(R.integer.transfer_amount_length)))

        contractAccountBalance = transferExtra(intent)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<TransferFormIntent> =Observable.merge(
        Observable.just(TransferFormIntent.Init(contractAccountBalance)),
        Observable.merge(
            RxView.clicks(account_transfer_button),
            RxTextView.editorActions(account_transfer_memo)
        ).map {
            hideKeyboard()
            TransferFormIntent.SubmitForm(
                TransferFormData(
                    contractAccountBalance,
                    account_transfer_to_input.text.toString(),
                    account_transfer_amount.text.toString(),
                    account_transfer_memo.text.toString()
                )
            )
        }
    )

    override fun layout(): TransferFormViewLayout = this

    override fun model(): TransferFormViewModel = getViewModel(viewModelFactory)

    override fun render(): TransferFormViewRenderer = render

    override fun populate(formattedBalance: String) {
        account_transfer_amount_label.text = getString(R.string.transfer_form_amount_label, formattedBalance)
    }

    override fun showValidationError(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(R.string.app_dialog_positive_button, null)
            .create()
            .show()
    }

    override fun navigateToConfirmation(transferFormData: TransferFormData) {
        model().publish(TransferFormIntent.Idle)
        startActivity(transferConfirmIntent(transferFormData, this))
    }

    companion object {

        private const val ACCOUNT_BALANCE_EXTRA = "ACCOUNT_BALANCE_EXTRA"

        fun transferFormIntent(contractAccountBalance: ContractAccountBalance, context: Context): Intent {
            return with (Intent(context, TransferFormActivity::class.java)) {
                putExtra(ACCOUNT_BALANCE_EXTRA, contractAccountBalance)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                this
            }
        }

        fun transferExtra(intent: Intent): ContractAccountBalance {
            return intent.getParcelableExtra(ACCOUNT_BALANCE_EXTRA) as ContractAccountBalance
        }
    }
}
