package com.memtrip.eosreach.app.account.resources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.memtrip.eos.core.utils.Pretty
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.account.EosAccountResource
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.account_resources_fragment.*
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

class ResourcesFragment
    : MviFragment<ResourcesIntent, ResourcesRenderAction, ResourcesViewState, ResourcesViewLayout>(), ResourcesViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: ResourcesViewRenderer

    private val pretty = Pretty()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.account_resources_fragment, container, false)
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<ResourcesIntent> {
        return Observable.just(ResourcesIntent.Init(fromBundle(arguments!!)))
    }

    override fun layout(): ResourcesViewLayout = this

    override fun model(): ResourcesViewModel = getViewModel(viewModelFactory)

    override fun render(): ResourcesViewRenderer = render

    override fun populate(eosAccount: EosAccount) {
        resources_ram_progressbar.progress = resourcePercentage(eosAccount.ramResource).toInt()
        resources_ram_values.text = getString(
            R.string.resources_ram_values,
            pretty.ram(eosAccount.ramResource.used.toFloat()),
            pretty.ram(eosAccount.ramResource.available.toFloat()))

        resources_cpu_progressbar.progress = resourcePercentage(eosAccount.cpuResource).toInt()
        resources_cpu_values.text = getString(
            R.string.resources_cpu_values,
            pretty.cpu(eosAccount.cpuResource.used.toFloat()),
            pretty.cpu(eosAccount.cpuResource.available.toFloat()))

        resources_net_progressbar.progress = resourcePercentage(eosAccount.netResource).toInt()
        resources_net_values.text = getString(
            R.string.resources_net_values,
            pretty.net(eosAccount.netResource.used.toFloat()),
            pretty.net(eosAccount.netResource.available.toFloat()))
    }

    private fun resourcePercentage(resource: EosAccountResource): Long {
        return if (resource.used <= 0) {
            resource.available
        } else {
            (resource.available / resource.used) * 100
        }
    }

    companion object {

        fun newInstance(eosAccount: EosAccount): ResourcesFragment = with (ResourcesFragment()) {
            arguments = toBundle(eosAccount)
            this
        }

        private fun toBundle(eosAccount: EosAccount): Bundle = with (Bundle()) {
            putParcelable("eosAccount", eosAccount)
            this
        }

        private fun fromBundle(bundle: Bundle): EosAccount = bundle.getParcelable("eosAccount")
    }
}
