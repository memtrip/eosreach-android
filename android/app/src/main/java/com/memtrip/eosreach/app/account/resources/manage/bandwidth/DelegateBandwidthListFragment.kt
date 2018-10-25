package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.api.bandwidth.DelegatedBandwidth
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.UndelegateBandwidthContainerActivity.Companion.undelegateBandwidthContainerIntent
import com.memtrip.eosreach.app.price.BalanceFormatter
import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible

import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.bandwidth_delegate_list_fragment.*
import kotlinx.android.synthetic.main.bandwidth_delegate_list_fragment.view.*
import javax.inject.Inject

class DelegateBandwidthListFragment
    : MviFragment<DelegateBandwidthListIntent, DelegateListRenderAction, DelegateBandwidthListViewState, DelegateListViewLayout>(), DelegateListViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: DelegateListViewRenderer

    private lateinit var adapter: DelegateBandwidthListAdapter
    private lateinit var contractAccountBalance: ContractAccountBalance

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bandwidth_delegate_list_fragment, container, false)
        val adapterInteraction: PublishSubject<Interaction<DelegatedBandwidth>> = PublishSubject.create()
        adapter = DelegateBandwidthListAdapter(context!!, adapterInteraction)
        contractAccountBalance = contractAccountBalance(arguments!!)
        view.bandwidth_delegate_list_recyclerview.adapter = adapter
        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<DelegateBandwidthListIntent> = Observable.merge(
        Observable.just(DelegateBandwidthListIntent.Init(contractAccountBalance.accountName)),
        bandwidth_delegate_list_error.retryClick().map {
            DelegateBandwidthListIntent.Init(contractAccountBalance.accountName)
        },
        adapter.interaction.map {
            DelegateBandwidthListIntent.NavigateToUndelegateBandwidth(it.data)
        }
    )

    override fun layout(): DelegateListViewLayout = this

    override fun model(): DelegateBandwidthListViewModel = getViewModel(viewModelFactory)

    override fun render(): DelegateListViewRenderer = render

    override fun showProgress() {
        bandwidth_delegate_list_progress.visible()
        bandwidth_delegate_list_recyclerview.gone()
        bandwidth_delegate_list_error.gone()
        bandwidth_delegate_list_empty.gone()
    }

    override fun showError() {
        bandwidth_delegate_list_progress.gone()
        bandwidth_delegate_list_error.visible()
        bandwidth_delegate_list_error.populate(
            getString(R.string.app_dialog_error_title),
            getString(R.string.bandwidth_delegated_list_error_body))
    }

    override fun populate(delegatedBandwidth: List<DelegatedBandwidth>) {
        bandwidth_delegate_list_progress.gone()
        bandwidth_delegate_list_recyclerview.visible()
        adapter.clear()
        adapter.populate(delegatedBandwidth)
    }

    override fun showEmptyBandwidth() {
        bandwidth_delegate_list_progress.gone()
        bandwidth_delegate_list_empty.visible()
    }

    override fun navigateToUndelegateBandwidth(delegatedBandwidth: DelegatedBandwidth) {
        model().publish(DelegateBandwidthListIntent.Idle)
        startActivity(undelegateBandwidthContainerIntent(BandwidthFormBundle(
            delegatedBandwidth.accountName,
            DelegateTarget.SELF,
            BalanceFormatter.deserialize(delegatedBandwidth.netWeight),
            BalanceFormatter.deserialize(delegatedBandwidth.cpuWeight)
        ), contractAccountBalance, activity!!))
    }

    companion object {

        private const val CONTRACT_ACCOUNT_BALANCE = "CONTRACT_ACCOUNT_BALANCE"

        fun newInstance(contractAccountBalance: ContractAccountBalance): DelegateBandwidthListFragment {
            return with(DelegateBandwidthListFragment()) {
                arguments = with(Bundle()) {
                    putParcelable(CONTRACT_ACCOUNT_BALANCE, contractAccountBalance)
                    this
                }
                this
            }
        }

        fun contractAccountBalance(bundle: Bundle): ContractAccountBalance =
            bundle.getParcelable(CONTRACT_ACCOUNT_BALANCE)
    }
}
