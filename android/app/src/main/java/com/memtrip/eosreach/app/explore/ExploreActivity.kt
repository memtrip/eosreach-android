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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jakewharton.rxbinding2.support.v4.view.RxViewPager
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.uikit.visible

import dagger.android.AndroidInjection
import io.reactivex.Observable
import javax.inject.Inject

import kotlinx.android.synthetic.main.explore_activity.*

class ExploreActivity
    : MviActivity<ExploreIntent, ExploreRenderAction, ExploreViewState, ExploreViewLayout>(), ExploreViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: ExploreViewRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.explore_activity)
        setSupportActionBar(explore_toolbar)
        supportActionBar!!.title = getString(R.string.explore_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<ExploreIntent> = Observable.merge(
        Observable.just(ExploreIntent.Init),
        RxViewPager.pageSelections(explore_viewpager).map { position ->
            when (position) {
                ExploreFragmentPagerFragment.Page.SEARCH.ordinal -> {
                    ExploreIntent.SearchTabIdle
                }
                ExploreFragmentPagerFragment.Page.BLOCK_PRODUCERS.ordinal -> {
                    ExploreIntent.BlockProducerTabIdle
                }
                else -> {
                    ExploreIntent.SearchTabIdle
                }
            }
        }
    )

    override fun layout(): ExploreViewLayout = this

    override fun model(): ExploreViewModel = getViewModel(viewModelFactory)

    override fun render(): ExploreViewRenderer = render

    override fun populate(page: ExploreFragmentPagerFragment.Page) {
        publishIdleTab(page)

        val exploreFragmentPagerFragment = ExploreFragmentPagerFragment(
            supportFragmentManager,
            this)
        explore_viewpager.adapter = exploreFragmentPagerFragment
        explore_viewpager.offscreenPageLimit = 2
        explore_viewpager.currentItem = page.ordinal
        explore_viewpager.visible()

        explore_tablayout.setupWithViewPager(explore_viewpager)
    }

    private fun publishIdleTab(page: ExploreFragmentPagerFragment.Page): Unit = when (page) {
        ExploreFragmentPagerFragment.Page.SEARCH -> {
            model().publish(ExploreIntent.SearchTabIdle)
        }
        ExploreFragmentPagerFragment.Page.BLOCK_PRODUCERS -> {
            model().publish(ExploreIntent.BlockProducerTabIdle)
        }
    }

    companion object {
        fun exploreIntent(context: Context): Intent {
            return Intent(context, ExploreActivity::class.java)
        }
    }
}
