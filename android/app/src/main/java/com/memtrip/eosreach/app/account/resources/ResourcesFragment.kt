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
package com.memtrip.eosreach.app.account.resources

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.StringRes
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eos.core.utils.Pretty
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.account.EosAccountResource
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.AccountTheme
import com.memtrip.eosreach.app.account.balance.BalanceFragment
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthManageActivity.Companion.manageBandwidthIntent
import com.memtrip.eosreach.app.account.resources.manage.manageram.ManageRamActivity.Companion.manageRamIntent
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.account_resources_fragment.*

import javax.inject.Inject

abstract class ResourcesFragment
    : MviFragment<ResourcesIntent, ResourcesRenderAction, ResourcesViewState, ResourcesViewLayout>(), ResourcesViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: ResourcesViewRenderer

    lateinit var eosAccount: EosAccount
    lateinit var contractAccountBalance: ContractAccountBalance

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.account_resources_fragment, container, false)
        eosAccount = eosAccountExtra(arguments!!)
        contractAccountBalance = contractAccountBalanceExtra(arguments!!)
        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<ResourcesIntent> = Observable.merge(
        Observable.just(ResourcesIntent.Init(eosAccount, contractAccountBalance)),
        RxView.clicks(resources_manage_bandwidth_button).map {
            ResourcesIntent.NavigateToManageBandwidth
        },
        RxView.clicks(resources_manage_ram_button).map {
            ResourcesIntent.NavigateToManageRam
        }
    )

    override fun layout(): ResourcesViewLayout = this

    override fun model(): ResourcesViewModel = getViewModel(viewModelFactory)

    override fun render(): ResourcesViewRenderer = render

    override fun showManageResourcesNavigation() {
        resources_manage_navigation_group.visible()
    }

    override fun hideManageResourcesNavigation() {
        resources_manage_navigation_group.gone()
    }

    override fun populate(eosAccount: EosAccount) {

        render(
            resources_ram_progressbar,
            resources_ram_values,
            R.string.resources_ram_values,
            eosAccount.ramResource
        ) {
            Pretty.ram(it)
        }

        render(
            resources_cpu_progressbar,
            resources_cpu_values,
            R.string.resources_cpu_values,
            eosAccount.cpuResource
        ) {
            Pretty.cpu(it)
        }

        render(
            resources_net_progressbar,
            resources_net_values,
            R.string.resources_net_values,
            eosAccount.netResource
        ) {
            Pretty.net(it)
        }
    }

    private fun render(
        progressBar: ProgressBar,
        valuesTextView: TextView,
        @StringRes formattingStringRes: Int,
        resource: EosAccountResource,
        pretty: (value: Long) -> String
    ) {
        progressBar.progress = resourcePercentage(resource).toInt()

        if (progressBar.progress == 0) {
            progressBar.background = with (progressBar.progressDrawable) {
                setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.MULTIPLY)
                this
            }
        }

        valuesTextView.text = getString(
            formattingStringRes,
            pretty(resourceRemaining(resource)),
            pretty(resource.available))
    }

    private fun resourceRemaining(resource: EosAccountResource): Long {
        return (resource.available - resource.used)
    }

    private fun resourcePercentage(resource: EosAccountResource): Long {
        return if (resource.used <= 0) {
            100
        } else if (resource.available == resource.used) {
            0
        } else {
            val remaining = resource.available.toFloat() - resource.used.toFloat()
            ((remaining * 100) / resource.available.toFloat()).toLong()
        }
    }

    override fun populateNetStake(staked: String) {
        resources_net_staked_value.text = staked
    }

    override fun emptyNetStake() {
        resources_net_staked_value.text = getString(R.string.app_empty_value)
    }

    override fun populateNetDelegated(delegated: String) {
        resources_net_delegated_value.text = delegated
    }

    override fun emptyNetDelegated() {
        resources_net_delegated_value.text = getString(R.string.app_empty_value)
    }

    override fun populateCpuStake(staked: String) {
        resources_cpu_staked_value.text = staked
    }

    override fun emptyCpuStake() {
        resources_cpu_staked_value.text = getString(R.string.app_empty_value)
    }

    override fun populateCpuDelegated(delegated: String) {
        resources_cpu_delegated_value.text = delegated
    }

    override fun emptyCpuDelegated() {
        resources_cpu_delegated_value.text = getString(R.string.app_empty_value)
    }

    override fun navigateToManageBandwidth() {
        model().publish(ResourcesIntent.Idle)
        startActivity(manageBandwidthIntent(
            eosAccount,
            contractAccountBalance,
            context!!))
    }

    override fun navigateToManageRam() {
        model().publish(ResourcesIntent.Idle)
        startActivity(manageRamIntent(
            eosAccount,
            contractAccountBalance,
            context!!))
    }

    companion object {

        private const val EOS_ACCOUNT_EXTRA = "EOS_ACCOUNT"
        private const val CONTRACT_ACCOUNT_BALANCE = "CONTRACT_ACCOUNT_BALANCE"

        fun newInstance(
            eosAccount: EosAccount,
            contractBalanceAccount: ContractAccountBalance,
            accountTheme: AccountTheme
        ): ResourcesFragment {
            return when(accountTheme) {
                AccountTheme.DEFAULT -> {
                    with(DefaultResourcesFragment()) {
                        arguments = with (Bundle()) {
                            putParcelable(EOS_ACCOUNT_EXTRA, eosAccount)
                            putParcelable(CONTRACT_ACCOUNT_BALANCE, contractBalanceAccount)
                            this
                        }
                        this
                    }
                }
                AccountTheme.READ_ONLY -> {
                    with(ReadOnlyResourcesFragment()) {
                        arguments = with (Bundle()) {
                            putParcelable(EOS_ACCOUNT_EXTRA, eosAccount)
                            putParcelable(CONTRACT_ACCOUNT_BALANCE, contractBalanceAccount)
                            this
                        }
                        this
                    }
                }
            }
        }

        private fun eosAccountExtra(bundle: Bundle): EosAccount =
            bundle.getParcelable(EOS_ACCOUNT_EXTRA)

        private fun contractAccountBalanceExtra(bundle: Bundle): ContractAccountBalance =
            bundle.getParcelable(CONTRACT_ACCOUNT_BALANCE)
    }
}