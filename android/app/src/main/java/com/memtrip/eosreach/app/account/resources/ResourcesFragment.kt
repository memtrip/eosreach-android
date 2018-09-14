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
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.ManageBandwidthActivity.Companion.manageBandwidthIntent
import com.memtrip.eosreach.app.account.resources.manage.manageram.ManageRamActivity.Companion.manageRamIntent
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.account_resources_fragment.*
import javax.inject.Inject

class ResourcesFragment
    : MviFragment<ResourcesIntent, ResourcesRenderAction, ResourcesViewState, ResourcesViewLayout>(), ResourcesViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: ResourcesViewRenderer

    lateinit var eosAccount: EosAccount

    private val pretty = Pretty()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.account_resources_fragment, container, false)
        eosAccount = eosAccountExtra(arguments!!)
        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<ResourcesIntent> = Observable.merge(
        Observable.just(ResourcesIntent.Init(eosAccount)),
        RxView.clicks(resources_manage_bandwidth_button).map {
            ResourcesIntent.NavigateToManageBandwidth
        },
        RxView.clicks(resources_unstake_resources_button).map {
            ResourcesIntent.NavigateToManageRam
        }
    )

    override fun layout(): ResourcesViewLayout = this

    override fun model(): ResourcesViewModel = getViewModel(viewModelFactory)

    override fun render(): ResourcesViewRenderer = render

    override fun populate(eosAccount: EosAccount) {

        render(
            resources_ram_progressbar,
            resources_ram_values,
            R.string.resources_ram_values,
            eosAccount.ramResource
        ) {
            pretty.ram(it)
        }

        render(
            resources_cpu_progressbar,
            resources_cpu_values,
            R.string.resources_cpu_values,
            eosAccount.cpuResource
        ) {
            pretty.cpu(it)
        }

        render(
            resources_net_progressbar,
            resources_net_values,
            R.string.resources_net_values,
            eosAccount.netResource
        ) {
            pretty.net(it)
        }
    }

    private fun render(
        progressBar: ProgressBar,
        valuesTextView: TextView,
        @StringRes formattingStringRes: Int,
        resource: EosAccountResource,
        pretty: (value: Float) -> String
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
            pretty(resource.available.toFloat()))
    }

    private fun resourceRemaining(resource: EosAccountResource): Float {
        return (resource.available - resource.used).toFloat()
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
            context!!))
    }

    override fun navigateToManageRam() {
        model().publish(ResourcesIntent.Idle)
        startActivity(manageRamIntent(eosAccount, context!!))
    }


    companion object {

        private const val EOS_ACCOUNT_EXTRA = "EOS_ACCOUNT"

        fun newInstance(eosAccount: EosAccount): ResourcesFragment = with (ResourcesFragment()) {
            arguments = with (Bundle()) {
                putParcelable(EOS_ACCOUNT_EXTRA, eosAccount)
                this
            }
            this
        }

        private fun eosAccountExtra(bundle: Bundle): EosAccount =
            bundle.getParcelable(EOS_ACCOUNT_EXTRA)
    }
}
