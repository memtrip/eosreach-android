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
package com.memtrip.eosreach.app.blockproducer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.blockproducer.BlockProducerDetails
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.AccountTheme
import com.memtrip.eosreach.app.blockproducer.ViewBlockProducerActivity.Companion.viewBlockProducerIntentWithDetails
import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.block_producer_list_activity.*
import javax.inject.Inject

class BlockProducerListActivity
    : MviActivity<BlockProducerListIntent, BlockProducerListRenderAction, BlockProducerListViewState, BlockProducerListViewLayout>(), BlockProducerListViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: BlockProducerListViewRenderer

    private lateinit var adapter: BlockProducerListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.block_producer_list_activity)

        val adapterInteraction: PublishSubject<Interaction<BlockProducerDetails>> = PublishSubject.create()
        adapter = BlockProducerListAdapter(this, adapterInteraction)
        block_producer_list_recyclerview.adapter = adapter

        setSupportActionBar(block_producer_list_toolbar)
        supportActionBar!!.title = getString(R.string.block_producer_list_toolbar_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<BlockProducerListIntent> = Observable.merge(
        Observable.just(BlockProducerListIntent.Init),
        block_producer_list_error_view.retryClick().map { BlockProducerListIntent.Retry },
        adapter.interaction.map {
            when (it.id) {
                R.id.block_producer_list_item_information ->
                    BlockProducerListIntent.BlockProducerInformationSelected(it.data)
                else ->
                    BlockProducerListIntent.BlockProducerSelected(it.data)
            }
        }
    )

    override fun layout(): BlockProducerListViewLayout = this

    override fun model(): BlockProducerListViewModel = getViewModel(viewModelFactory)

    override fun render(): BlockProducerListViewRenderer = render

    override fun showProgress() {
        block_producer_list_progressbar.visible()
        block_producer_list_error_view.gone()
        block_producer_list_recyclerview.gone()
    }

    override fun showError() {
        block_producer_list_progressbar.gone()
        block_producer_list_error_view.visible()
        block_producer_list_error_view.populate(
            getString(R.string.block_producer_list_error_title),
            getString(R.string.block_producer_list_error_body))
    }

    override fun populate(blockProducerList: List<BlockProducerDetails>) {
        block_producer_list_progressbar.gone()
        block_producer_list_recyclerview.visible()
        adapter.clear()
        adapter.populate(blockProducerList)
    }

    override fun blockProducerSelected(blockProducerDetails: BlockProducerDetails) {
        model().publish(BlockProducerListIntent.Idle)
        setResult(RESULT_CODE, resultIntent(blockProducerDetails))
        finish()
    }

    override fun blockProducerInformationSelected(blockProducerDetails: BlockProducerDetails) {
        model().publish(BlockProducerListIntent.Idle)
        startActivity(viewBlockProducerIntentWithDetails(AccountTheme.DEFAULT, blockProducerDetails, this))
    }

    companion object {

        const val RESULT_CODE = 0x9001

        private const val BLOCK_PRODUCER_DETAILS: String = "BLOCK_PRODUCER_DETAILS"

        fun blockProducerListIntent(context: Context): Intent {
            return Intent(context, BlockProducerListActivity::class.java)
        }

        fun blockProducerDetailsFromIntent(intent: Intent): BlockProducerDetails {
            return intent.getParcelableExtra(BLOCK_PRODUCER_DETAILS) as BlockProducerDetails
        }

        fun resultIntent(blockProducerDetails: BlockProducerDetails): Intent {
            return with(Intent()) {
                putExtra(BLOCK_PRODUCER_DETAILS, blockProducerDetails)
            }
        }
    }
}
