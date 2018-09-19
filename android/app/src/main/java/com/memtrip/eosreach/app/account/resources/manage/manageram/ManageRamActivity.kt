package com.memtrip.eosreach.app.account.resources.manage.manageram

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
import kotlinx.android.synthetic.main.manage_ram_activity.*
import javax.inject.Inject

class ManageRamActivity
    : MviActivity<ManageRamIntent, ManageRamRenderAction, ManageRamViewState, ManageRamViewLayout>(), ManageRamViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: ManageRamViewRenderer

    lateinit var eosAccount: EosAccount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manage_ram_activity)
        setSupportActionBar(manage_ram_toolbar)
        supportActionBar!!.title = getString(R.string.resources_manage_ram_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        eosAccount = eosAccountExtra(intent)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<ManageRamIntent> = Observable.merge(
        Observable.just(ManageRamIntent.Init(eosAccount)),
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

    override fun populate(eosAccount: EosAccount, page: ManageRamFragmentPagerAdapter.Page) {
        model().publish(ManageRamIntent.BuyRamTabIdle)

        val manageRamFragmentPagerAdapter = ManageRamFragmentPagerAdapter(
            supportFragmentManager,
            this,
            eosAccount)

        manage_ram_viewpager.adapter = manageRamFragmentPagerAdapter
        manage_ram_viewpager.offscreenPageLimit = 2
        manage_ram_viewpager.currentItem = page.ordinal
        manage_ram_viewpager.visible()

        manage_ram_tablayout.setupWithViewPager(manage_ram_viewpager)
    }

    companion object {

        private const val EOS_ACCOUNT_EXTRA = "EOS_ACCOUNT_EXTRA"

        fun manageRamIntent(eosAccount: EosAccount, context: Context): Intent {
            return with (Intent(context, ManageRamActivity::class.java)) {
                putExtra(EOS_ACCOUNT_EXTRA, eosAccount)
                this
            }
        }

        private fun eosAccountExtra(intent: Intent): EosAccount {
            return intent.getParcelableExtra(EOS_ACCOUNT_EXTRA) as EosAccount
        }
    }
}
