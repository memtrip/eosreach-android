package com.memtrip.eosreach.app.account.resources.manage.manageram.form

import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.account.resources.manage.manageram.RamCommitType
import com.memtrip.eosreach.app.account.resources.manage.manageram.RamFormFragment

class SellRamFormFragment : RamFormFragment() {

    override fun buttonLabel(): String = context!!.getString(R.string.resources_manage_ram_form_sell_button)
    override val ramCommitType: RamCommitType = RamCommitType.SELL

    companion object {

        fun newInstance(
            eosAccount: EosAccount,
            contractAccountBalance: ContractAccountBalance,
            ramPricePerKb: Balance
        ): SellRamFormFragment {
            return with (SellRamFormFragment()) {
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