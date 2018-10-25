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
package com.memtrip.eosreach.app.account.vote.cast

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jakewharton.rxbinding2.support.v4.view.RxViewPager
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.uikit.visible
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
        setSupportActionBar(account_cast_vote_toolbar)
        supportActionBar!!.title = getString(R.string.vote_cast_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        eosAccount = eosAccountExtra(intent)
        page = pageExtra(intent)
        account_cast_vote_viewpager.currentItem = page.ordinal
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<CastVoteIntent> = Observable.merge(
        Observable.just(CastVoteIntent.Init(eosAccount, page)),
        RxViewPager.pageSelections(account_cast_vote_viewpager).map { position ->
            when (position) {
                CastVoteFragmentPagerFragment.Page.PRODUCER.ordinal -> {
                    CastVoteIntent.CastProducerVoteTabIdle
                }
                CastVoteFragmentPagerFragment.Page.PROXY.ordinal -> {
                    CastVoteIntent.CastProxyVoteTabIdle
                }
                else -> {
                    CastVoteIntent.CastProducerVoteTabIdle
                }
            }
        }
    )

    override fun layout(): CastVoteViewLayout = this

    override fun model(): CastVoteViewModel = getViewModel(viewModelFactory)

    override fun render(): CastVoteViewRenderer = render

    override fun onStart() {
        super.onStart()
        model().publish(CastVoteIntent.CastProducerVoteTabIdle)
    }

    override fun populate(page: CastVoteFragmentPagerFragment.Page) {

        publishIdleTab(page)

        val castVotePagerFragment = CastVoteFragmentPagerFragment(
            supportFragmentManager,
            this,
            eosAccount)
        account_cast_vote_viewpager.adapter = castVotePagerFragment
        account_cast_vote_viewpager.offscreenPageLimit = 2
        account_cast_vote_viewpager.currentItem = page.ordinal
        account_cast_vote_viewpager.visible()

        account_cast_vote_tablayout.setupWithViewPager(account_cast_vote_viewpager)
    }

    private fun publishIdleTab(page: CastVoteFragmentPagerFragment.Page): Unit = when (page) {
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
            return with(Intent(context, CastVoteActivity::class.java)) {
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
