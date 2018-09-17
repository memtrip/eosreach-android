package com.memtrip.eosreach.app.account.vote.cast

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.AccountFragmentPagerAdapter
import com.memtrip.eosreach.app.account.AccountIntent
import com.memtrip.eosreach.uikit.visible
import com.memtrip.eosreach.utils.ViewPagerOnPageChangeListener
import dagger.android.AndroidInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.account_cast_vote_activity.*
import javax.inject.Inject

class CastVoteActivity
    : MviActivity<CastVoteIntent, CastVoteRenderAction, CastVoteViewState, CastVoteViewLayout>(), CastVoteViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: CastVoteViewRenderer

    private lateinit var eosAccount: EosAccount
    private lateinit var page: CastVoteFragmentPagerFragment.Page

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_cast_vote_activity)
        setSupportActionBar(cast_producers_vote_toolbar)
        supportActionBar!!.title = getString(R.string.vote_cast_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        eosAccount = eosAccountExtra(intent)
        page = pageExtra(intent)
        cast_producer_vote_viewpager.currentItem = page.ordinal
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<CastVoteIntent> {
        return Observable.just(CastVoteIntent.Init(eosAccount, page))
    }

    override fun layout(): CastVoteViewLayout = this

    override fun model(): CastVoteViewModel = getViewModel(viewModelFactory)

    override fun render(): CastVoteViewRenderer = render

    override fun populate(eosAccount: EosAccount, page: CastVoteFragmentPagerFragment.Page) {

        publishIdleTab(page)

        val castVotePagerFragment = CastVoteFragmentPagerFragment(
            supportFragmentManager,
            this,
            eosAccount)
        cast_producer_vote_viewpager.adapter = castVotePagerFragment
        cast_producer_vote_viewpager.offscreenPageLimit = 2
        cast_producer_vote_viewpager.currentItem = page.ordinal
        cast_producer_vote_viewpager.visible()
        cast_producer_vote_viewpager.addOnPageChangeListener(object : ViewPagerOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    CastVoteFragmentPagerFragment.Page.PRODUCER.ordinal -> {
                        model().publish(CastVoteIntent.CastProducerVoteTabIdle)
                    }
                    CastVoteFragmentPagerFragment.Page.PROXY.ordinal -> {
                        model().publish(CastVoteIntent.CastProxyVoteTabIdle)
                    }
                    else -> {
                        model().publish(CastVoteIntent.CastProducerVoteTabIdle)
                    }
                }
            }
        })

        cast_producer_vote_tablayout.setupWithViewPager(cast_producer_vote_viewpager)
    }

    private fun publishIdleTab(page: CastVoteFragmentPagerFragment.Page): Unit = when(page) {
        CastVoteFragmentPagerFragment.Page.PRODUCER -> {
            model().publish(CastVoteIntent.CastProducerVoteTabIdle)
        }
        CastVoteFragmentPagerFragment.Page.PROXY -> {
            model().publish(CastVoteIntent.CastProxyVoteTabIdle)
        }
    }

    companion object {

        private const val EOS_ACCOUNT_EXTRA = "EOS_ACCOUNT_EXTRA"
        private const val PAGE_EXTRA = "PAGE"

        const val CAST_VOTE_REQUEST_CODE = 0x0002
        const val CAST_VOTE_RESULT_CODE = 0x9002

        fun castVoteIntent(
            eosAccount: EosAccount,
            page: CastVoteFragmentPagerFragment.Page,
            context: Context
        ): Intent {
            return with (Intent(context, CastVoteActivity::class.java)) {
                putExtra(EOS_ACCOUNT_EXTRA, eosAccount)
                putExtra(PAGE_EXTRA, page)
                this
            }
        }

        private fun eosAccountExtra(intent: Intent): EosAccount =
            intent.getParcelableExtra(EOS_ACCOUNT_EXTRA)

        private fun pageExtra(intent: Intent): CastVoteFragmentPagerFragment.Page =
            intent.getSerializableExtra(PAGE_EXTRA) as CastVoteFragmentPagerFragment.Page
    }
}
