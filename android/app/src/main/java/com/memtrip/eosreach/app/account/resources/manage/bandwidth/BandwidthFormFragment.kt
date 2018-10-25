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
package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthConfirmActivity.Companion.confirmBandwidthIntent
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.inputfilter.AccountNameInputFilter
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

    private lateinit var contractAccountBalance: ContractAccountBalance
    private lateinit var bandwidthFormBundle: BandwidthFormBundle

    abstract fun rootViewId(): Int

    abstract fun buttonLabel(): String

    abstract val bandwidthCommitType: BandwidthCommitType

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.manage_bandwidth_form_fragment, container, false)
        contractAccountBalance = contractAccountBalanceExtra(arguments!!)
        bandwidthFormBundle = bandwidthFormBundle(arguments!!)
        view.manage_bandwidth_target_account_form_input.filters = arrayOf(
            AccountNameInputFilter(),
            InputFilter.LengthFilter(context!!.resources.getInteger(R.integer.app_account_name_length)))
        view.manage_bandwidth_net_amount_form_input.filters = arrayOf(CurrencyFormatInputFilter())
        view.manage_bandwidth_cpu_amount_form_input.filters = arrayOf(CurrencyFormatInputFilter())
        view.manage_bandwidth_form_cta_button.text = buttonLabel()
        view.id = rootViewId()
        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<BandwidthFormIntent> = Observable.merge(
        Observable.just(BandwidthFormIntent.Init(contractAccountBalance, bandwidthFormBundle)),
        RxView.clicks(manage_bandwidth_form_cta_button).map {
            BandwidthFormIntent.Confirm(
                bandwidthCommitType,
                manage_bandwidth_net_amount_form_input.editableText.toString(),
                manage_bandwidth_cpu_amount_form_input.editableText.toString(),
                manage_bandwidth_target_account_form_input.editableText.toString(),
                manage_bandwidth_transfer_perm_checkbox.isChecked,
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

    override fun stakeSelfAccountName(accountName: String) {
        manage_bandwidth_transfer_perm_checkbox.gone()
        manage_bandwidth_target_account_form_input.isEnabled = false
        manage_bandwidth_target_account_form_input.setText(accountName)
        manage_bandwidth_target_account_form_input.setBackgroundResource(R.drawable.view_button_transparent_background_rounded)
    }

    override fun stakedCpu(cpu: String) {
        manage_bandwidth_cpu_amount_form_label.text = getString(R.string.resources_manage_bandwidth_delegate_form_cpu_stake_label)
        manage_bandwidth_cpu_amount_form_input.setText(cpu)
    }

    override fun stakedNet(net: String) {
        manage_bandwidth_net_amount_form_label.text = getString(R.string.resources_manage_bandwidth_delegate_form_net_stake_label)
        manage_bandwidth_net_amount_form_input.setText(net)
    }

    override fun navigateToConfirm(bandwidthBundle: BandwidthBundle) {
        model().publish(BandwidthFormIntent.Idle)
        startActivity(confirmBandwidthIntent(bandwidthBundle, contractAccountBalance, context!!))
    }

    companion object {

        private const val CONTRACT_ACCOUNT_BALANCE = "CONTRACT_ACCOUNT_BALANCE"
        private const val BANDWIDTH_FORM_BUNDLE = "BANDWIDTH_FORM_BUNDLE"

        fun toBundle(
            bandwidthFormBundle: BandwidthFormBundle,
            contractAccountBalance: ContractAccountBalance
        ): Bundle = with(Bundle()) {
            putParcelable(BANDWIDTH_FORM_BUNDLE, bandwidthFormBundle)
            putParcelable(CONTRACT_ACCOUNT_BALANCE, contractAccountBalance)
            this
        }

        private fun bandwidthFormBundle(bundle: Bundle): BandwidthFormBundle =
            bundle.getParcelable(BANDWIDTH_FORM_BUNDLE)

        private fun contractAccountBalanceExtra(bundle: Bundle): ContractAccountBalance =
            bundle.getParcelable(CONTRACT_ACCOUNT_BALANCE)
    }
}
