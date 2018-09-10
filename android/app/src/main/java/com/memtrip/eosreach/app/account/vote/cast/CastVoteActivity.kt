package com.memtrip.eosreach.app.account.vote.cast

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.AccountIntent
import com.memtrip.eosreach.app.account.AccountPagerFragment
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible
import com.memtrip.eosreach.utils.ViewPagerOnPageChangeListener
import dagger.android.AndroidInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.account_activity.*
import kotlinx.android.synthetic.main.account_cast_vote_activity.*
import javax.inject.Inject

class CastVoteActivity
    : MviActivity<CastVoteIntent, CastVoteRenderAction, CastVoteViewState, CastVoteViewLayout>(), CastVoteViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: CastVoteViewRenderer

    lateinit var eosAccount: EosAccount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_cast_vote_activity)
        eosAccount = fromIntent(intent!!)
        setSupportActionBar(cast_producer_vote_toolbar)
        supportActionBar!!.title = getString(R.string.cast_vote_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<CastVoteIntent> = Observable.just(CastVoteIntent.Init(eosAccount))

    override fun layout(): CastVoteViewLayout = this

    override fun model(): CastVoteViewModel = getViewModel(viewModelFactory)

    override fun render(): CastVoteViewRenderer = render

    override fun populate(eosAccount: EosAccount) {
        model().publish(CastVoteIntent.CastProducerVoteTabIdle)

        val castVotePagerFragment = CastVotePagerFragment(
            supportFragmentManager,
            this,
            eosAccount)
        cast_producer_vote_viewpager.adapter = castVotePagerFragment
        cast_producer_vote_viewpager.offscreenPageLimit = 2
        cast_producer_vote_viewpager.visible()
        cast_producer_vote_viewpager.addOnPageChangeListener(object : ViewPagerOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    CastVotePagerFragment.Page.PRODUCER.ordinal -> {
                        model().publish(CastVoteIntent.CastProducerVoteTabIdle)
                    }
                    CastVotePagerFragment.Page.PROXY.ordinal -> {
                        model().publish(CastVoteIntent.CastProxyVoteTabIdle)
                    }
                }
            }
        })

        cast_producer_vote_tablayout.setupWithViewPager(cast_producer_vote_viewpager)
    }

    override fun selectCastProducerVoteTab() {
        cast_producer_vote_viewpager.currentItem = CastVotePagerFragment.Page.PRODUCER.ordinal
    }

    override fun selectCastProxyVoteTab() {
        cast_producer_vote_viewpager.currentItem = CastVotePagerFragment.Page.PROXY.ordinal
    }

    companion object {

        private const val EOS_ACCOUNT_EXTRA = "EOS_ACCOUNT_EXTRA"

        const val CAST_VOTE_REQUEST_CODE = 0x0002
        const val CAST_VOTE_RESULT_CODE = 0x9002

        fun castVoteIntent(eosAccount: EosAccount, context: Context): Intent {
            return with (Intent(context, CastVoteActivity::class.java)) {
                putExtra(EOS_ACCOUNT_EXTRA, eosAccount)
                this
            }
        }

        private fun fromIntent(intent: Intent): EosAccount {
            return intent.getParcelableExtra(EOS_ACCOUNT_EXTRA) as EosAccount
        }
    }
}
