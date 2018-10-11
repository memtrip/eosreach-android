/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.memtrip.eosreach.app.proxyvoter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.facebook.drawee.drawable.ScalingUtils
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.proxyvoter.ProxyVoterDetails
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.AccountBundle
import com.memtrip.eosreach.app.account.ReadonlyAccountActivity.Companion.accountReadOnlyIntent
import com.memtrip.eosreach.app.blockproducer.ViewBlockProducerActivity
import com.memtrip.eosreach.app.blockproducer.ViewBlockProducerDisplayAction
import com.memtrip.eosreach.app.blockproducer.ViewBlockProducerIntent
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible

import dagger.android.AndroidInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.proxy_voter_view_activity.*
import javax.inject.Inject

class ViewProxyVoterActivity
    : MviActivity<ViewProxyVoterIntent, ViewProxyVoterRenderAction, ViewProxyVoterViewState, ViewProxyVoterViewLayout>(), ViewProxyVoterViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: ViewProxyVoterViewRenderer

    lateinit var proxyVoterDetails: ProxyVoterDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.proxy_voter_view_activity)

        setSupportActionBar(proxy_voter_view_toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        proxy_voter_view_owner_account_button.setOnClickListener {
            model().publish(ViewProxyVoterIntent.Idle)
            startActivity(accountReadOnlyIntent(AccountBundle(proxyVoterDetails.owner), this))
        }
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<ViewProxyVoterIntent> = Observable.merge(
        startingIntent(displayAction(intent)),
        RxView.clicks(proxy_voter_view_website_button).map {
            ViewProxyVoterIntent.NavigateToUrl(proxyVoterDetails.website)
        },
        proxy_voter_view_error_view.retryClick().map {
            ViewProxyVoterIntent.Retry(proxyVoterAccountName(intent))
        }
    )

    private fun startingIntent(displayAction: ViewProxyVoterDisplayAction): Observable<ViewProxyVoterIntent> {
        return when (displayAction) {
            ViewProxyVoterDisplayAction.DETAILS -> {
                Observable.just(ViewProxyVoterIntent.InitWithDetails(proxyVoterDetails(intent)))
            }
            ViewProxyVoterDisplayAction.LOAD -> {
                Observable.just(ViewProxyVoterIntent.InitWithName(proxyVoterAccountName(intent)))
            }
        }
    }

    override fun layout(): ViewProxyVoterViewLayout = this

    override fun model(): ViewProxyVoterViewModel = getViewModel(viewModelFactory)

    override fun render(): ViewProxyVoterViewRenderer = render

    override fun showProgress() {
        proxy_voter_view_group.gone()
        proxy_voter_view_error_view.gone()
        proxy_voter_view_progress.visible()
    }

    override fun showError() {
        proxy_voter_view_progress.gone()
        proxy_voter_view_error_view.visible()
        proxy_voter_view_error_view.populate(
            getString(R.string.proxy_voter_view_error_title),
            getString(R.string.proxy_voter_view_error_body))
    }

    override fun populate(proxyVoterDetails: ProxyVoterDetails) {

        proxy_voter_view_group.visible()
        proxy_voter_view_progress.gone()

        this.proxyVoterDetails = proxyVoterDetails
        supportActionBar!!.title = proxyVoterDetails.owner
        proxy_voter_view_candidate_name_label.text = proxyVoterDetails.name

        if (proxyVoterDetails.slogan.trim().isNotEmpty()) {
            proxy_voter_view_slogan_label.text = proxyVoterDetails.slogan
        } else {
            proxy_voter_view_slogan_label.text = getString(R.string.app_empty_value)
        }

        if (proxyVoterDetails.philosophy.trim().isNotEmpty()) {
            proxy_voter_view_philosophy_label.text = proxyVoterDetails.philosophy
        } else {
            proxy_voter_view_philosophy_label.text = getString(R.string.app_empty_value)
        }

        if (proxyVoterDetails.logo256.isNotEmpty()) {
            proxy_voter_view_icon.hierarchy.actualImageScaleType = ScalingUtils.ScaleType.CENTER_CROP
            proxy_voter_view_icon.setImageURI(proxyVoterDetails.logo256)
        } else {
            proxy_voter_view_icon.setActualImageResource(R.drawable.app_toolbar_eosio_logo)
        }
    }

    override fun invalidUrl(url: String) {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.proxy_voter_view_invalid_url, url))
            .setPositiveButton(R.string.app_dialog_positive_button, null)
            .create()
            .show()
    }

    override fun navigateToUrl(url: String) {
        model().publish(ViewProxyVoterIntent.Idle)
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    companion object {

        private const val PROXY_VOTER_DETAILS = "PROXY_VOTER_DETAILS"
        private const val PROXY_VOTER_ACCOUNT_NAME = "PROXY_VOTER_ACCOUNT_NAME"
        private const val PROXY_VOTER_DISPLAY_ACTION = "PROXY_VOTER_DISPLAY_ACTION"


        fun viewProxyVoterIntentWithDetails(
            proxyVoterDetails: ProxyVoterDetails,
            displayAction: ViewProxyVoterDisplayAction,
            context: Context
        ): Intent {
            return with (Intent(context, ViewProxyVoterActivity::class.java)) {
                putExtra(PROXY_VOTER_DETAILS, proxyVoterDetails)
                putExtra(PROXY_VOTER_DISPLAY_ACTION, displayAction)
                this
            }
        }

        fun viewProxyVoterIntentWithName(
            accountName: String,
            displayAction: ViewProxyVoterDisplayAction,
            context: Context
        ): Intent {
            return with (Intent(context, ViewProxyVoterActivity::class.java)) {
                putExtra(PROXY_VOTER_ACCOUNT_NAME, accountName)
                putExtra(PROXY_VOTER_DISPLAY_ACTION, displayAction)
                this
            }
        }

        fun proxyVoterAccountName(intent: Intent): String = intent.getStringExtra(PROXY_VOTER_ACCOUNT_NAME)

        fun proxyVoterDetails(intent: Intent): ProxyVoterDetails = intent.getParcelableExtra(PROXY_VOTER_DETAILS)

        private fun displayAction(intent: Intent): ViewProxyVoterDisplayAction {
            return intent.getSerializableExtra(PROXY_VOTER_DISPLAY_ACTION) as ViewProxyVoterDisplayAction
        }
    }
}