package com.memtrip.eosreach.app.account.resources.manage.bandwidth.form

import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthCommitType
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthFormFragment

class DelegateBandwidthFormFragment : BandwidthFormFragment() {
    override fun buttonLabel(): String = context!!.getString(R.string.resources_manage_bandwidth_delegate_form_delegate_button)
    override val bandwidthCommitType: BandwidthCommitType = BandwidthCommitType.DELEGATE

    companion object {

        fun newInstance(
            eosAccount: EosAccount,
            contractAccountBalance: ContractAccountBalance
        ): DelegateBandwidthFormFragment {
            return with (DelegateBandwidthFormFragment()) {
                arguments = BandwidthFormFragment.toBundle(eosAccount, contractAccountBalance)
                this
            }
        }
    }
}