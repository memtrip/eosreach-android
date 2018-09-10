package com.memtrip.eosreach.app.account.resources

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.StringRes
import com.memtrip.eos.core.utils.Pretty
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.account.EosAccountResource
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.AccountPagerFragment
import com.memtrip.eosreach.app.account.AccountParentRefresh
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.account_resources_fragment.*
import kotlinx.android.synthetic.main.account_resources_fragment.view.*
import javax.inject.Inject

class ResourcesFragment
    : MviFragment<ResourcesIntent, ResourcesRenderAction, ResourcesViewState, ResourcesViewLayout>(), ResourcesViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: ResourcesViewRenderer

    lateinit var accountParentRefresh: AccountParentRefresh

    private val pretty = Pretty()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        accountParentRefresh = context as AccountParentRefresh
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.account_resources_fragment, container, false)
        view.account_resources_swipelayout.setOnRefreshListener {
            accountParentRefresh.triggerRefresh(AccountPagerFragment.Page.RESOURCES)
        }
        return view
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
