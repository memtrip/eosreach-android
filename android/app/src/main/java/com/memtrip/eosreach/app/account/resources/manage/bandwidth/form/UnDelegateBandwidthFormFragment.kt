package com.memtrip.eosreach.app.account.resources.manage.bandwidth.form

import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount

import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthCommitType
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthFormFragment

class UnDelegateBandwidthFormFragment : BandwidthFormFragment() {
    override fun buttonLabel(): String = context!!.getString(R.string.manage_bandwidth_delegate_form_undelegate_button)
    override val bandwidthCommitType: BandwidthCommitType = BandwidthCommitType.UNDELEGATE

    companion object {

        fun newInstance(eosAccount: EosAccount): UnDelegateBandwidthFormFragment {
            return with (UnDelegateBandwidthFormFragment()) {
                arguments = BandwidthFormFragment.toBundle(eosAccount)
                this
            }
        }
    }
}