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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.proxyvoter.ProxyVoterDetails
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.proxyvoter.ViewProxyVoterActivity.Companion.viewProxyVoterIntentWithDetails
import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible

import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

import kotlinx.android.synthetic.main.proxy_voter_list_activity.*

class ProxyVoterListActivity
    : MviActivity<ProxyVoterListIntent, ProxyVoterListRenderAction, ProxyVoterListViewState, ProxyVoterListViewLayout>(), ProxyVoterListViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: ProxyVoterListViewRenderer

    private lateinit var adapter: ProxyVoterListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.proxy_voter_list_activity)

        val adapterInteraction: PublishSubject<Interaction<ProxyVoterDetails>> = PublishSubject.create()
        adapter = ProxyVoterListAdapter(this, adapterInteraction)
        proxy_voter_list_recyclerview.adapter = adapter

        setSupportActionBar(proxy_voter_list_toolbar)
        supportActionBar!!.title = getString(R.string.proxy_voter_list_toolbar_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<ProxyVoterListIntent> = Observable.merge(
        Observable.just(ProxyVoterListIntent.Init),
        proxy_voter_list_error_view.retryClick().map {
            ProxyVoterListIntent.Retry
        },
        adapter.interaction.map { row ->
            when (row.id) {
                R.id.proxy_voter_list_recyclerview -> {
                ProxyVoterListIntent.LoadMoreProxyVoters(row.data.owner)
                }
                R.id.proxy_voter_list_item_information -> {
                ProxyVoterListIntent.ProxyVoterInformationSelected(row.data)
                }else -> {
                ProxyVoterListIntent.ProxyVoterSelected(row.data)
                }
            }
        }
    )

    override fun layout(): ProxyVoterListViewLayout = this

    override fun model(): ProxyVoterListViewModel = getViewModel(viewModelFactory)

    override fun render(): ProxyVoterListViewRenderer = render

    override fun showProgress() {
        proxy_voter_list_progress.visible()
        proxy_voter_list_recyclerview.gone()
        proxy_voter_list_error_view.gone()
    }

    override fun showError() {
        proxy_voter_list_progress.gone()
        proxy_voter_list_error_view.visible()
        proxy_voter_list_error_view.populate(
            getString(R.string.proxy_voter_list_error_title),
            getString(R.string.proxy_voter_list_error_body))
    }

    override fun populate(voteProxies: List<ProxyVoterDetails>) {
        model().publish(ProxyVoterListIntent.Idle)
        proxy_voter_list_progress.gone()
        proxy_voter_list_recyclerview.visible()
        adapter.populate(voteProxies)
    }

    override fun navigateToProxyVoterDetails(proxyVoterDetails: ProxyVoterDetails) {
        model().publish(ProxyVoterListIntent.Idle)
        startActivity(viewProxyVoterIntentWithDetails(proxyVoterDetails, ViewProxyVoterDisplayAction.DETAILS, this))
    }

    override fun selectProxyVoter(proxyVoterDetails: ProxyVoterDetails) {
        model().publish(ProxyVoterListIntent.Idle)
        setResult(RESULT_CODE, resultIntent(proxyVoterDetails))
        finish()
    }

    override fun loadMoreProgress() {
    }

    override fun loadMoreError() {
    }

    companion object {

        const val RESULT_CODE = 0x9002

        private const val PROXY_VOTER_DETAILS: String = "PROXY_VOTER_DETAILS"

        fun proxyVoterListIntent(context: Context): Intent {
            return Intent(context, ProxyVoterListActivity::class.java)
        }

        fun proxyVoterDetailsFromIntent(intent: Intent): ProxyVoterDetails {
            return intent.getParcelableExtra(PROXY_VOTER_DETAILS) as ProxyVoterDetails
        }

        fun resultIntent(proxyVoterDetails: ProxyVoterDetails): Intent {
            return with (Intent()) {
                putExtra(PROXY_VOTER_DETAILS, proxyVoterDetails)
            }
        }
    }
}
