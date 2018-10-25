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
package com.memtrip.eosreach.app.account.resources.manage.manageram

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jakewharton.rxbinding2.support.v4.view.RxViewPager
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible
import dagger.android.AndroidInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.manage_ram_activity.*
import javax.inject.Inject

class ManageRamActivity
    : MviActivity<ManageRamIntent, ManageRamRenderAction, ManageRamViewState, ManageRamViewLayout>(), ManageRamViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: ManageRamViewRenderer

    lateinit var eosAccount: EosAccount
    lateinit var contractAccountBalance: ContractAccountBalance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manage_ram_activity)
        setSupportActionBar(manage_ram_toolbar)
        supportActionBar!!.title = getString(R.string.resources_manage_ram_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        eosAccount = eosAccountExtra(intent)
        contractAccountBalance = contractAccountBalance(intent)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<ManageRamIntent> = Observable.merge(
        Observable.just(ManageRamIntent.Init(contractAccountBalance.balance.symbol)),
        RxViewPager.pageSelections(manage_ram_viewpager).map {
            when (it) {
                ManageRamFragmentPagerAdapter.Page.BUY.ordinal ->
                    ManageRamIntent.BuyRamTabIdle
                ManageRamFragmentPagerAdapter.Page.SELL.ordinal ->
                    ManageRamIntent.SellRamTabIdle
                else ->
                    ManageRamIntent.BuyRamTabIdle
            }
        }
    )

    override fun layout(): ManageRamViewLayout = this

    override fun model(): ManageRamViewModel = getViewModel(viewModelFactory)

    override fun render(): ManageRamViewRenderer = render

    override fun showProgress() {
        manage_ram_group.gone()
        manage_ram_error.gone()
        manage_ram_progress.visible()
    }

    override fun showRamPriceError() {
        manage_ram_progress.gone()
        manage_ram_error.populate(
            getString(R.string.app_dialog_error_title),
            getString(R.string.resources_manage_ram_error)
        )
    }

    override fun populate(
        ramPricePerKb: Balance,
        formattedRamPrice: String,
        page: ManageRamFragmentPagerAdapter.Page
    ) {

        model().publish(ManageRamIntent.BuyRamTabIdle)

        manage_ram_progress.gone()
        manage_ram_group.visible()
        manage_ram_current_price_value.text = formattedRamPrice

        val manageRamFragmentPagerAdapter = ManageRamFragmentPagerAdapter(
            supportFragmentManager,
            this,
            eosAccount,
            contractAccountBalance,
            ramPricePerKb
        )

        manage_ram_viewpager.adapter = manageRamFragmentPagerAdapter
        manage_ram_viewpager.offscreenPageLimit = 2
        manage_ram_viewpager.currentItem = page.ordinal
        manage_ram_viewpager.visible()

        manage_ram_tablayout.setupWithViewPager(manage_ram_viewpager)
    }

    companion object {

        private const val EOS_ACCOUNT_EXTRA = "EOS_ACCOUNT_EXTRA"
        private const val CONTRACT_ACCOUNT_BALANCE = "CONTRACT_ACCOUNT_BALANCE"

        fun manageRamIntent(
            eosAccount: EosAccount,
            contractAccountBalance: ContractAccountBalance,
            context: Context
        ): Intent {
            return with(Intent(context, ManageRamActivity::class.java)) {
                putExtra(EOS_ACCOUNT_EXTRA, eosAccount)
                putExtra(CONTRACT_ACCOUNT_BALANCE, contractAccountBalance)
                this
            }
        }

        private fun eosAccountExtra(intent: Intent): EosAccount {
            return intent.getParcelableExtra(EOS_ACCOUNT_EXTRA)
        }

        private fun contractAccountBalance(intent: Intent): ContractAccountBalance {
            return intent.getParcelableExtra(CONTRACT_ACCOUNT_BALANCE)
        }
    }
}
