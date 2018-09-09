package com.memtrip.eosreach.app.price.currencypairing

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.welcome.EntryActivity.Companion.entryIntent
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.invisible
import com.memtrip.eosreach.uikit.visible
import dagger.android.AndroidInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.currency_pairing_activity.*
import javax.inject.Inject

class CurrencyPairingActivity
    : MviActivity<CurrencyPairingIntent, CurrencyPairingRenderAction, CurrencyPairingViewState, CurrencyPairingViewLayout>(), CurrencyPairingViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: CurrencyPairingViewRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.currency_pairing_activity)
        setSupportActionBar(currency_pairing_toolbar)
        supportActionBar!!.title = getString(R.string.currency_pairing_toolbar_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<CurrencyPairingIntent> {
        return Observable.merge(
            RxTextView.editorActions(currency_pairing_currency_pairing_input),
            RxView.clicks(currency_pairing_pair_button)
        ).map {
            CurrencyPairingIntent.CurrencyPair(currency_pairing_currency_pairing_input.text.toString())
        }
    }

    override fun layout(): CurrencyPairingViewLayout = this

    override fun model(): CurrencyPairingViewModel = getViewModel(viewModelFactory)

    override fun render(): CurrencyPairingViewRenderer = render

    override fun showProgress() {
        currency_pairing_progress.visible()
        currency_pairing_pair_button.invisible()
        hideKeyboard()
    }

    override fun showError(message: String) {
        currency_pairing_progress.gone()
        currency_pairing_pair_button.visible()

        AlertDialog.Builder(this)
            .setTitle(R.string.app_dialog_error_title)
            .setMessage(message)
            .setPositiveButton(R.string.app_dialog_positive_button, null)
            .create()
            .show()
    }

    override fun onSuccess() {
        currency_pairing_progress.gone()
        currency_pairing_pair_button.visible()

        startActivity(entryIntent(this))
        finish()
    }

    companion object {

        fun currencyPairingIntent(context: Context): Intent {
            return Intent(context, CurrencyPairingActivity::class.java)
        }
    }
}
