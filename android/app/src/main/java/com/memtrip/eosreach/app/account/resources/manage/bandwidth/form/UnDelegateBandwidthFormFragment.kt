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
package com.memtrip.eosreach.app.account.resources.manage.bandwidth.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthCommitType
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthFormBundle
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthFormFragment
import com.memtrip.eosreach.uikit.gone
import kotlinx.android.synthetic.main.manage_bandwidth_form_fragment.view.*

class UnDelegateBandwidthFormFragment : BandwidthFormFragment() {
    override fun rootViewId(): Int = R.id.account_resources_undelegate_bandwidth_fragment
    override fun buttonLabel(): String = context!!.getString(R.string.resources_manage_bandwidth_delegate_form_undelegate_button)
    override val bandwidthCommitType: BandwidthCommitType = BandwidthCommitType.UNDELEGATE

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)!!
        view.manage_bandwidth_transfer_perm_checkbox.gone()
        return view
    }

    companion object {

        fun newInstance(
            bandwidthFormBundle: BandwidthFormBundle,
            contractAccountBalance: ContractAccountBalance
        ): UnDelegateBandwidthFormFragment {
            return with(UnDelegateBandwidthFormFragment()) {
                arguments = BandwidthFormFragment.toBundle(bandwidthFormBundle, contractAccountBalance)
                this
            }
        }
    }
}