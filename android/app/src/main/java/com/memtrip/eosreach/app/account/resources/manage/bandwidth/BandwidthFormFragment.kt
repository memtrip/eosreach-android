package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthConfirmActivity.Companion.confirmBandwidthIntent
import com.memtrip.eosreach.uikit.inputfilter.CurrencyFormatInputFilter

import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.manage_bandwidth_form_fragment.*
import kotlinx.android.synthetic.main.manage_bandwidth_form_fragment.view.*
import javax.inject.Inject

abstract class BandwidthFormFragment
    : MviFragment<BandwidthFormIntent, BandwidthFormRenderAction, BandwidthFormViewState, BandwidthFormViewLayout>(), BandwidthFormViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: BandwidthFormViewRenderer

    private lateinit var eosAccount: EosAccount
    private lateinit var contractAccountBalance: ContractAccountBalance

    abstract fun buttonLabel(): String

    abstract val bandwidthCommitType: BandwidthCommitType

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.manage_bandwidth_form_fragment, container, false)
        eosAccount = eosAccountExtra(arguments!!)
        contractAccountBalance = contractAccountBalanceExtra(arguments!!)
        view.manage_bandwidth_net_amount_form_input.filters = arrayOf(CurrencyFormatInputFilter())
        view.manage_bandwidth_cpu_amount_form_input.filters = arrayOf(CurrencyFormatInputFilter())
        view.manage_bandwidth_form_cta_button.text = buttonLabel()
        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<BandwidthFormIntent> = Observable.merge(
        Observable.just(BandwidthFormIntent.Init(contractAccountBalance)),
        Observable.merge(
            RxView.clicks(manage_bandwidth_form_cta_button),
            RxTextView.editorActions(manage_bandwidth_cpu_amount_form_input)
        ).map {
            BandwidthFormIntent.Confirm(
                bandwidthCommitType,
                manage_bandwidth_net_amount_form_input.editableText.toString(),
                manage_bandwidth_cpu_amount_form_input.editableText.toString(),
                eosAccount.accountName,
                contractAccountBalance)
        }
    )

    override fun layout(): BandwidthFormViewLayout = this

    override fun model(): BandwidthFormViewModel = getViewModel(viewModelFactory)

    override fun render(): BandwidthFormViewRenderer = render

    override fun populate(formattedBalance: String) {
        manage_bandwidth_cpu_amount_form_label.text = getString(R.string.resources_manage_bandwidth_delegate_form_cpu_amount_label, formattedBalance)
        manage_bandwidth_net_amount_form_label.text = getString(R.string.resources_manage_bandwidth_delegate_form_net_amount_label, formattedBalance)
    }

    override fun navigateToConfirm(bandwidthBundle: BandwidthBundle) {
        model().publish(BandwidthFormIntent.Idle)
        startActivity(confirmBandwidthIntent(bandwidthBundle, contractAccountBalance, context!!))
    }

    companion object {

        private const val EOS_ACCOUNT = "EOS_ACCOUNT"
        private const val CONTRACT_ACCOUNT_BALANCE = "CONTRACT_ACCOUNT_BALANCE"

        fun toBundle(
            eosAccount: EosAccount,
            contractAccountBalance: ContractAccountBalance
        ): Bundle = with (Bundle()) {
            putParcelable(EOS_ACCOUNT, eosAccount)
            putParcelable(CONTRACT_ACCOUNT_BALANCE, contractAccountBalance)
            this
        }

        private fun eosAccountExtra(bundle: Bundle): EosAccount =
            bundle.getParcelable(EOS_ACCOUNT)

        private fun contractAccountBalanceExtra(bundle: Bundle): ContractAccountBalance =
            bundle.getParcelable(CONTRACT_ACCOUNT_BALANCE)
    }
}
