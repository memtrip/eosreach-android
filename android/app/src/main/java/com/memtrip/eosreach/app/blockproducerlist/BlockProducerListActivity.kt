package com.memtrip.eosreach.app.blockproducerlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.accountlist.AccountListAdapter
import com.memtrip.eosreach.app.accountlist.AccountListIntent
import com.memtrip.eosreach.app.settings.SettingsActivity
import com.memtrip.eosreach.db.account.AccountEntity
import com.memtrip.eosreach.db.blockproducer.BlockProducerEntity
import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible

import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.accounts_list_activity.*
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

        val adapterInteraction: PublishSubject<Interaction<BlockProducerEntity>> = PublishSubject.create()
        adapter = BlockProducerListAdapter(this, adapterInteraction)
        blockproducer_list_recyclerview.adapter = adapter

        setSupportActionBar(blockproducer_list_toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<BlockProducerListIntent> = Observable.merge(
        Observable.just(BlockProducerListIntent.Init),
        blockproducer_list_error_view.retryClick().map { BlockProducerListIntent.Retry },
        adapter.interaction.map {
            BlockProducerListIntent.BlockProducerSelected(it.data)
        }
    )

    override fun layout(): BlockProducerListViewLayout = this

    override fun model(): BlockProducerListViewModel = getViewModel(viewModelFactory)

    override fun render(): BlockProducerListViewRenderer = render

    override fun showProgress() {
        blockproducer_list_progressbar.visible()
        blockproducer_list_error_view.gone()
        blockproducer_list_recyclerview.gone()
    }

    override fun showError() {
        blockproducer_list_progressbar.gone()
        blockproducer_list_error_view.visible()
        blockproducer_list_error_view.populate(
            getString(R.string.blockproducer_list_error_title),
            getString(R.string.blockproducer_list_error_body))
    }

    override fun populate(blockProducerList: List<BlockProducerEntity>) {
        blockproducer_list_progressbar.gone()
        blockproducer_list_recyclerview.visible()
        adapter.clear()
        adapter.populate(blockProducerList)
    }

    override fun blockProducerSelected(blockProducer: BlockProducerEntity) {
        setResult(RESULT_CODE, toIntent(BlockProducerBundle(
            blockProducer.accountName,
            blockProducer.candidateName,
            blockProducer.apiUrl,
            blockProducer.logoUrl
        )))
        finish()
    }

    companion object {

        const val RESULT_CODE = 0x9001

        private const val BLOCK_PRODUCER_BUNDLE: String = "BLOCK_PRODUCER_BUNDLE"

        fun blockProducerList(context: Context): Intent {
            return Intent(context, BlockProducerListActivity::class.java)
        }

        fun fromIntent(intent: Intent): BlockProducerBundle {
            return intent.getParcelableExtra(BLOCK_PRODUCER_BUNDLE) as BlockProducerBundle
        }

        fun toIntent(blockProducerBundle: BlockProducerBundle): Intent {
            return with (Intent()) {
                putExtra(BLOCK_PRODUCER_BUNDLE, blockProducerBundle)
            }
        }
    }
}
