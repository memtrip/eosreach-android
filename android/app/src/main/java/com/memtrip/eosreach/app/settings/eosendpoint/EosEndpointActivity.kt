package com.memtrip.eosreach.app.settings.eosendpoint

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.blockproducerlist.BlockProducerListActivity
import com.memtrip.eosreach.app.blockproducerlist.BlockProducerListActivity.Companion.blockProducerList
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.invisible
import com.memtrip.eosreach.uikit.visible
import dagger.android.AndroidInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.eos_endpoint_activity.*
import javax.inject.Inject

class EosEndpointActivity
    : MviActivity<EosEndpointIntent, EosEndpointRenderAction, EosEndpointViewState, EosEndpointViewLayout>(), EosEndpointViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: EosEndpointViewRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.eos_endpoint_activity)
        setSupportActionBar(eos_endpoint_activity_toolbar)
        supportActionBar!!.title = getString(R.string.eos_endpoint_toolbar_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == BlockProducerListActivity.RESULT_CODE) {
            val blockProducerBundle = BlockProducerListActivity.fromIntent(data!!)
            eos_endpoint_url_input.setText(blockProducerBundle.apiUrl, TextView.BufferType.EDITABLE)
            model().publish(EosEndpointIntent.ChangeEndpoint(blockProducerBundle.apiUrl))
        }
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<EosEndpointIntent> = Observable.merge(
        Observable.merge(
            RxView.clicks(eos_endpoint_change_button),
            RxTextView.editorActions(eos_endpoint_url_input)
        ).map {
            EosEndpointIntent.ChangeEndpoint(eos_endpoint_url_input.text.toString())
        },
        RxView.clicks(account_cast_vote_endpoint_block_producer).map { EosEndpointIntent.NavigateToBlockProducerList }
    )

    override fun layout(): EosEndpointViewLayout = this

    override fun model(): EosEndpointViewModel = getViewModel(viewModelFactory)

    override fun render(): EosEndpointViewRenderer = render

    override fun showProgress() {
        hideKeyboard()
        eos_endpoint_progress.visible()
        eos_endpoint_change_button.invisible()
    }

    override fun showError(message: String) {
        hideKeyboard()
        eos_endpoint_progress.gone()
        eos_endpoint_change_button.visible()

        AlertDialog.Builder(this)
            .setTitle(R.string.app_dialog_error_title)
            .setMessage(message)
            .setPositiveButton(R.string.app_dialog_positive_button, null)
            .create()
            .show()
    }

    override fun currentUrl(url: String) {
        eos_endpoint_current_url_value.text = url
    }

    override fun onSuccess() {
        eos_endpoint_progress.gone()
        eos_endpoint_change_button.visible()

        AlertDialog.Builder(this)
            .setTitle(R.string.eos_endpoint_updated_title)
            .setMessage(R.string.eos_endpoint_updated_body)
            .setPositiveButton(R.string.eos_endpoint_updated_exit_app) { _, _ ->
                finishAffinity()
            }
            .setOnCancelListener {
                finishAffinity()
            }
            .create()
            .show()
    }

    override fun navigateToBlockProducerList() {
        model().publish(EosEndpointIntent.Idle)
        startActivityForResult(blockProducerList(this), BlockProducerListActivity.RESULT_CODE)
    }

    companion object {

        fun eosEndpointIntent(context: Context): Intent {
            return Intent(context, EosEndpointActivity::class.java)
        }
    }
}
