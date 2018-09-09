package com.memtrip.eosreach.app.issue.createaccount

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputFilter
import android.view.WindowManager
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviActivity

import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.welcome.EntryActivity
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.inputfilter.AccountNameInputFilter
import com.memtrip.eosreach.uikit.inputfilter.CurrencyFormatInputFilter
import com.memtrip.eosreach.uikit.invisible
import com.memtrip.eosreach.uikit.visible
import io.reactivex.Observable
import kotlinx.android.synthetic.main.issue_create_account_activity.*
import javax.inject.Inject

abstract class CreateAccountActivity
    : MviActivity<CreateAccountIntent, CreateAccountRenderAction, CreateAccountViewState, CreateAccountViewLayout>(), CreateAccountViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: CreateAccountViewRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.issue_create_account_activity)
        setSupportActionBar(issue_create_account_toolbars)
        supportActionBar!!.title = getString(R.string.issue_create_account_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE)

        issue_create_account_name_input.filters = arrayOf(
            AccountNameInputFilter(),
            InputFilter.LengthFilter(resources.getInteger(R.integer.transfer_username_length)))
    }

    override fun intents(): Observable<CreateAccountIntent> = Observable.merge(
        Observable.merge(
            RxView.clicks(issue_create_account_create_button),
            RxTextView.editorActions(issue_create_account_name_input)
        ).map {
            CreateAccountIntent.CreateAccount(
                issue_create_account_name_input.text.toString(),
                ""
            ) // TODO: this is sent as the result of purchasing a google play token
        },
        RxView.clicks(issue_create_account_done_button).map {
            CreateAccountIntent.Done(
                issue_create_account_private_key_label.text.toString())
        }
    )

    override fun layout(): CreateAccountViewLayout = this

    override fun model(): CreateAccountViewModel = getViewModel(viewModelFactory)

    override fun render(): CreateAccountViewRenderer = render

    override fun showCreateAccountProgress() {
        issue_create_account_progress.visible()
        issue_create_account_create_button.invisible()
    }

    override fun showCreateAccountError(error: String) {
        issue_create_account_progress.gone()
        issue_create_account_create_button.visible()

        AlertDialog.Builder(this)
            .setTitle(R.string.app_dialog_error_title)
            .setMessage(error)
            .setPositiveButton(R.string.app_dialog_positive_button, null)
            .create()
            .show()
    }

    override fun showImportKeyProgress() {
        issue_create_account_import_key_error.gone()
        issue_create_account_import_key_progress.visible()
    }

    override fun showImportKeyError(error: String) {
        issue_create_account_import_key_progress.gone()
        issue_create_account_import_key_error.visible()
        issue_create_account_import_key_error.populate(
            getString(R.string.issue_create_account_import_key_error_title),
            error
        )
    }

    override fun showAccountCreated(privateKey: String) {
        issue_create_account_form_group.gone()
        issue_create_account_private_key_group.visible()
        issue_create_account_private_key_label.text = privateKey
    }

    override fun navigateToAccountList() {
        startActivity(EntryActivity.entryIntent(this))
        finish()
    }

    abstract override fun inject()
}