package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jakewharton.rxbinding2.support.v4.view.RxViewPager
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.uikit.visible

import dagger.android.AndroidInjection
import io.reactivex.Observable
import javax.inject.Inject

import kotlinx.android.synthetic.main.manage_bandwidth_activity.*

class ManageBandwidthActivity
    : MviActivity<ManageBandwidthIntent, ManageBandwidthRenderAction, ManageBandwidthViewState, ManageBandwidthViewLayout>(), ManageBandwidthViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: ManageBandwidthViewRenderer

    private lateinit var eosAccount: EosAccount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manage_bandwidth_activity)
        setSupportActionBar(manage_bandwidth_toolbar)
        supportActionBar!!.title = getString(R.string.manage_bandwidth_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        eosAccount = eosAccountExtra(intent)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<ManageBandwidthIntent> = Observable.merge(
        Observable.just(ManageBandwidthIntent.Init(eosAccount)),
        RxViewPager.pageSelections(manage_bandwidth_viewpager).map {
            when (it) {
                ManageBandwidthFragmentPagerAdapter.Page.DELEGATE.ordinal ->
                    ManageBandwidthIntent.DelegateBandwidthTabIdle
                ManageBandwidthFragmentPagerAdapter.Page.UNDELEGATE.ordinal ->
                    ManageBandwidthIntent.UnDelegateBandwidthTabIdle
                else ->
                    ManageBandwidthIntent.DelegateBandwidthTabIdle
            }
        }
    )

    override fun layout(): ManageBandwidthViewLayout = this

    override fun model(): ManageBandwidthViewModel = getViewModel(viewModelFactory)

    override fun render(): ManageBandwidthViewRenderer = render

    override fun populate(eosAccount: EosAccount, page: ManageBandwidthFragmentPagerAdapter.Page) {
        model().publish(ManageBandwidthIntent.DelegateBandwidthTabIdle)

        val manageBandwidthFragmentPagerAdapter = ManageBandwidthFragmentPagerAdapter(
            supportFragmentManager,
            this,
            eosAccount)

        manage_bandwidth_viewpager.adapter = manageBandwidthFragmentPagerAdapter
        manage_bandwidth_viewpager.offscreenPageLimit = 2
        manage_bandwidth_viewpager.currentItem = page.ordinal
        manage_bandwidth_viewpager.visible()

        manage_bandwidth_tablayout.setupWithViewPager(manage_bandwidth_viewpager)
    }

    companion object {

        private const val EOS_ACCOUNT_EXTRA = "EOS_ACCOUNT_EXTRA"

        fun manageBandwidthIntent(
            eosAccount: EosAccount,
            context: Context
        ): Intent {
            return with (Intent(context, ManageBandwidthActivity::class.java)) {
                putExtra(EOS_ACCOUNT_EXTRA, eosAccount)
                this
            }
        }

        private fun eosAccountExtra(intent: Intent): EosAccount {
            return intent.getParcelableExtra(EOS_ACCOUNT_EXTRA) as EosAccount
        }
    }
}
