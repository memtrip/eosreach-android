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
package com.memtrip.eosreach.app.explore

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.blockproducer.BlockProducerDetails
import com.memtrip.eosreach.api.blockproducer.RegisteredBlockProducer
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.AccountBundle
import com.memtrip.eosreach.app.account.ReadonlyAccountActivity.Companion.accountReadOnlyIntent
import com.memtrip.eosreach.app.proxyvoter.ProxyVoterListAdapter
import com.memtrip.eosreach.app.proxyvoter.ProxyVoterListIntent
import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible

import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

import javax.inject.Inject

import kotlinx.android.synthetic.main.registered_block_producers_fragment.*
import kotlinx.android.synthetic.main.registered_block_producers_fragment.view.*

class RegisteredBlockProducersFragment
    : MviFragment<RegisteredBlockProducersIntent, RegisteredBlockProducersRenderAction, RegisteredBlockProducersViewState, RegisteredBlockProducersViewLayout>(), RegisteredBlockProducersViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: RegisteredBlockProducersViewRenderer

    private lateinit var adapter: RegisteredBlockProducerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.registered_block_producers_fragment, container, false)
        val adapterInteraction: PublishSubject<Interaction<RegisteredBlockProducer>> = PublishSubject.create()
        adapter = RegisteredBlockProducerAdapter(context!!, adapterInteraction)
        view.registered_block_producers_recyclerview.adapter = adapter
        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<RegisteredBlockProducersIntent> = Observable.merge(
        Observable.just(RegisteredBlockProducersIntent.Init),
        registered_block_producers_error.retryClick().map {
            RegisteredBlockProducersIntent.Retry
        },
        adapter.interaction.map { row ->
            when (row.id) {
                R.id.registered_block_producers_recyclerview -> {
                    RegisteredBlockProducersIntent.LoadMore(row.data.owner)
                }
                R.id.registered_block_producer_list_item_information -> {
                    RegisteredBlockProducersIntent.WebsiteSelected(row.data.url)
                }
                else -> {
                RegisteredBlockProducersIntent.RegisteredBlockProducersSelected(row.data.owner)
                }
            }
        }
    )

    override fun layout(): RegisteredBlockProducersViewLayout = this

    override fun model(): RegisteredBlockProducersViewModel = getViewModel(viewModelFactory)

    override fun render(): RegisteredBlockProducersViewRenderer = render

    override fun showProgress() {
        registered_block_producers_progress.visible()
    }

    override fun showEmpty() {
        registered_block_producers_progress.gone()
        registered_block_producers_error.visible()
        registered_block_producers_error.populate(
            getString(R.string.app_dialog_error_title),
            getString(R.string.explore_registered_block_producers_empty))
    }

    override fun showError() {
        registered_block_producers_progress.gone()
        registered_block_producers_error.visible()
        registered_block_producers_error.populate(
            getString(R.string.app_dialog_error_title),
            getString(R.string.explore_registered_block_producers_error))
    }

    override fun showLoadMoreProgress() {
    }

    override fun showLoadMoreError() {
    }

    override fun websiteSelected(url: String) {
        model().publish(RegisteredBlockProducersIntent.Idle)
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    override fun invalidWebsite(url: String) {
        AlertDialog.Builder(context!!)
            .setMessage(getString(R.string.proxy_voter_view_invalid_url, url))
            .setPositiveButton(R.string.app_dialog_positive_button, null)
            .create()
            .show()
    }

    override fun selectBlockProducer(accountName: String) {
        model().publish(RegisteredBlockProducersIntent.Idle)
        startActivity(accountReadOnlyIntent(
            AccountBundle(accountName),
            context!!
        ))
    }

    override fun populate(registeredBlockProducers: List<RegisteredBlockProducer>) {
        registered_block_producers_progress.gone()
        registered_block_producers_recyclerview.visible()
        adapter.populate(registeredBlockProducers)
    }

    companion object {
        fun newInstance(): RegisteredBlockProducersFragment {
            return RegisteredBlockProducersFragment()
        }
    }
}
