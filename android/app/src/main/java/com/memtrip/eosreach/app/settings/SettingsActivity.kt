package com.memtrip.eosreach.app.settings

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.price.currencypairing.CurrencyPairingActivity.Companion.currencyPairingIntent
import com.memtrip.eosreach.app.settings.eosendpoint.EosEndpointActivity.Companion.eosEndpointIntent
import com.memtrip.eosreach.app.settings.viewprivatekeys.ViewPrivateKeysActivity.Companion.viewPrivateKeysIntent
import com.memtrip.eosreach.app.transaction.log.ViewConfirmedTransactionsActivity.Companion.viewConfirmedTransactionsIntent
import com.memtrip.eosreach.app.welcome.EntryActivity.Companion.entryIntent
import dagger.android.AndroidInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.settings_activity.*
import javax.inject.Inject

class SettingsActivity
    : MviActivity<SettingsIntent, SettingsRenderAction, SettingsViewState, SettingsViewLayout>(), SettingsViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: SettingsViewRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        setSupportActionBar(settings_toolbar)
        supportActionBar!!.title = getString(R.string.settings_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<SettingsIntent> = Observable.mergeArray(
        Observable.just(SettingsIntent.Init),
        RxView.clicks(settings_exchange_rate_currency_button).map {
            SettingsIntent.NavigateToCurrencyPairing
        },
        RxView.clicks(settings_view_private_keys_button).map {
            SettingsIntent.NavigateToPrivateKeys
        },
        RxView.clicks(settings_change_eos_endpoint_button).map {
            SettingsIntent.NavigateToEosEndpoint
        },
        RxView.clicks(settings_view_confirmed_transactions_button).map {
            SettingsIntent.NavigateToViewConfirmedTransactions
        },
        RxView.clicks(settings_telegram_button).map {
            SettingsIntent.NavigateToTelegram
        },
        RxView.clicks(settings_clear_data_and_logout_button).map {
            SettingsIntent.RequestClearDataAndLogout
        },
        RxView.clicks(settings_exchange_view_credits_button).map {
            SettingsIntent.NavigateToAuthor
        }
    )

    override fun layout(): SettingsViewLayout = this

    override fun model(): SettingsViewModel = getViewModel(viewModelFactory)

    override fun render(): SettingsViewRenderer = render

    override fun navigateToCurrencyPairing() {
        model().publish(SettingsIntent.Idle)
        startActivity(currencyPairingIntent(this))
    }

    override fun navigateToEosEndpoint() {
        model().publish(SettingsIntent.Idle)
        startActivity(eosEndpointIntent(this))
    }

    override fun navigateToViewPrivateKeys() {
        model().publish(SettingsIntent.Idle)
        startActivity(viewPrivateKeysIntent(this))
    }

    override fun navigateToViewConfirmedTransactionLog() {
        model().publish(SettingsIntent.Idle)
        startActivity(viewConfirmedTransactionsIntent(this))
    }

    override fun populate(exchangeRateCurrency: String) {
        settings_exchange_rate_currency_value.text = exchangeRateCurrency
    }

    override fun navigateToTelegramGroup() {
        model().publish(SettingsIntent.Idle)
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(
            getString(R.string.settings_telegram_url)
        )))
    }

    override fun confirmClearData() {
        model().publish(SettingsIntent.Idle)
        AlertDialog.Builder(this)
            .setTitle(R.string.settings_clear_data_warning_dialog_title)
            .setMessage(R.string.settings_clear_data_warning_dialog_body)
            .setPositiveButton(R.string.app_dialog_positive_button) { _, _ ->
                model().publish(SettingsIntent.ConfirmClearDataAndLogout)
            }
            .setNegativeButton(R.string.app_dialog_negative_button, null)
            .create()
            .show()
    }

    override fun navigateToEntry() {
        startActivity(entryIntent(this))
        finish()
    }

    override fun navigateToAuthor() {
        model().publish(SettingsIntent.Idle)
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(
            getString(R.string.settings_author_url)
        )))
    }

    companion object {

        fun settingsIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }
}
