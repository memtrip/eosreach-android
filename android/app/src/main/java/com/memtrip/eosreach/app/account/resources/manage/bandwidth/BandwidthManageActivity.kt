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
package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jakewharton.rxbinding2.support.v4.view.RxViewPager
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.uikit.visible
import dagger.android.AndroidInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.manage_bandwidth_activity.*
import javax.inject.Inject

class BandwidthManageActivity
    : MviActivity<BandwidthManageIntent, BandwidthManageRenderAction, BandwidthManageViewState, BandwidthManageViewLayout>(), BandwidthManageViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: ManageBandwidthViewRenderer

    private lateinit var bandwidthFormBundle: BandwidthFormBundle
    private lateinit var contractAccountBalance: ContractAccountBalance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manage_bandwidth_activity)
        setSupportActionBar(manage_bandwidth_toolbar)
        supportActionBar!!.title = getString(R.string.resources_manage_bandwidth_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        bandwidthFormBundle = bandwidthFormBundleExtra(intent)
        contractAccountBalance = contractAccountBalanceExtra(intent)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<BandwidthManageIntent> = Observable.merge(
        Observable.just(BandwidthManageIntent.Init),
        RxViewPager.pageSelections(manage_bandwidth_viewpager).map {
            when (it) {
                BandwidthManageFragmentPagerAdapter.Page.DELEGATE.ordinal ->
                    BandwidthManageIntent.DelegateBandwidthTabIdle
                BandwidthManageFragmentPagerAdapter.Page.UNDELEGATE.ordinal ->
                    BandwidthManageIntent.UnDelegateBandwidthTabIdle
                BandwidthManageFragmentPagerAdapter.Page.ALLOCATED.ordinal ->
                    BandwidthManageIntent.AllocatedTabIdle
                else ->
                    BandwidthManageIntent.DelegateBandwidthTabIdle
            }
        }
    )

    override fun layout(): BandwidthManageViewLayout = this

    override fun model(): BandwidthManageViewModel = getViewModel(viewModelFactory)

    override fun render(): ManageBandwidthViewRenderer = render

    override fun populate(page: BandwidthManageFragmentPagerAdapter.Page) {
        model().publish(BandwidthManageIntent.DelegateBandwidthTabIdle)

        val fragmentPagerAdapter = BandwidthManageFragmentPagerAdapter(
            supportFragmentManager,
            this,
            bandwidthFormBundle,
            contractAccountBalance)

        manage_bandwidth_viewpager.adapter = fragmentPagerAdapter
        manage_bandwidth_viewpager.offscreenPageLimit = 3
        manage_bandwidth_viewpager.currentItem = page.ordinal
        manage_bandwidth_viewpager.visible()

        manage_bandwidth_tablayout.setupWithViewPager(manage_bandwidth_viewpager)
    }

    companion object {

        private const val BANDWIDTH_FORM_BUNDLE = "BANDWIDTH_FORM_BUNDLE"
        private const val CONTRACT_ACCOUNT_BALANCE_EXTRA = "CONTRACT_ACCOUNT_BALANCE_EXTRA"

        fun manageBandwidthIntent(
            bandwidthFormBundle: BandwidthFormBundle,
            contractAccountBalance: ContractAccountBalance,
            context: Context
        ): Intent {
            return with(Intent(context, BandwidthManageActivity::class.java)) {
                putExtra(BANDWIDTH_FORM_BUNDLE, bandwidthFormBundle)
                putExtra(CONTRACT_ACCOUNT_BALANCE_EXTRA, contractAccountBalance)
                this
            }
        }

        private fun bandwidthFormBundleExtra(intent: Intent): BandwidthFormBundle =
            intent.getParcelableExtra(BANDWIDTH_FORM_BUNDLE)

        private fun contractAccountBalanceExtra(intent: Intent): ContractAccountBalance =
            intent.getParcelableExtra(CONTRACT_ACCOUNT_BALANCE_EXTRA)
    }
}
