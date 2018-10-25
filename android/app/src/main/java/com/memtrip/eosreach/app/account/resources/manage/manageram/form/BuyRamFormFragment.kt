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
package com.memtrip.eosreach.app.account.resources.manage.manageram.form

import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.eosreach.api.balance.ContractAccountBalance

import com.memtrip.eosreach.app.account.resources.manage.manageram.RamCommitType
import com.memtrip.eosreach.app.account.resources.manage.manageram.RamFormFragment

class BuyRamFormFragment : RamFormFragment() {
    override fun rootViewId(): Int = R.id.account_resources_manage_ram_buy_fragment
    override fun buttonLabel(): String = context!!.getString(R.string.resources_manage_ram_form_buy_button)
    override val ramCommitType: RamCommitType = RamCommitType.BUY

    companion object {

        fun newInstance(
            eosAccount: EosAccount,
            contractAccountBalance: ContractAccountBalance,
            ramPricePerKb: Balance
        ): BuyRamFormFragment {
            return with(BuyRamFormFragment()) {
                arguments = RamFormFragment.toBundle(
                    eosAccount,
                    contractAccountBalance,
                    ramPricePerKb
                )
                this
            }
        }
    }
}