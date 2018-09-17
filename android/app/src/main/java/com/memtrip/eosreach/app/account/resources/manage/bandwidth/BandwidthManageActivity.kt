package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jakewharton.rxbinding2.support.v4.view.RxViewPager
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.uikit.visible

import dagger.android.AndroidInjection
import io.reactivex.Observable
import javax.inject.Inject

import kotlinx.android.synthetic.main.manage_bandwidth_activity.*

class BandwidthManageActivity
    : MviActivity<BandwidthManageIntent, BandwidthManageRenderAction, BandwidthManageViewState, BandwidthManageViewLayout>(), BandwidthManageViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: ManageBandwidthViewRenderer

    private lateinit var eosAccount: EosAccount
    private lateinit var contractAccountBalance: ContractAccountBalance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manage_bandwidth_activity)
        setSupportActionBar(manage_bandwidth_toolbar)
        supportActionBar!!.title = getString(R.string.resources_manage_bandwidth_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        eosAccount = eosAccountExtra(intent)
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
            eosAccount,
            contractAccountBalance)

        manage_bandwidth_viewpager.adapter = fragmentPagerAdapter
        manage_bandwidth_viewpager.offscreenPageLimit = 2
        manage_bandwidth_viewpager.currentItem = page.ordinal
        manage_bandwidth_viewpager.visible()

        manage_bandwidth_tablayout.setupWithViewPager(manage_bandwidth_viewpager)
    }

    companion object {

        private const val EOS_ACCOUNT_EXTRA = "EOS_ACCOUNT_EXTRA"
        private const val CONTRACT_ACCOUNT_BALANCE = "CONTRACT_ACCOUNT_BALANCE"

        fun manageBandwidthIntent(
            eosAccount: EosAccount,
            contractAccountBalance: ContractAccountBalance,
            context: Context
        ): Intent {
            return with (Intent(context, BandwidthManageActivity::class.java)) {
                putExtra(EOS_ACCOUNT_EXTRA, eosAccount)
                putExtra(CONTRACT_ACCOUNT_BALANCE, contractAccountBalance)
                this
            }
        }

        private fun eosAccountExtra(intent: Intent): EosAccount =
            intent.getParcelableExtra(EOS_ACCOUNT_EXTRA)

        private fun contractAccountBalanceExtra(intent: Intent): ContractAccountBalance =
            intent.getParcelableExtra(CONTRACT_ACCOUNT_BALANCE)
    }
}
